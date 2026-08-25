<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <!-- <el-main>：主要区域容器。 -->
  <el-main>
    <!-- 搜索栏 -->
    <el-form
        :model="searchParm"
        label-width="80px"
        :inline="true"
        size="default"
    >
      <el-form-item>
        <el-input
            v-model="searchParm.supplierName"
            placeholder="请输入供应商名称"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
            v-model="searchParm.linkUser"
            placeholder="请输入负责人"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
            v-model="searchParm.linkPhone"
            placeholder="请输入负责人电话"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" plain @click="searchBtn">搜索</el-button>
        <el-button type="danger" icon="Close" plain @click="resetBtn"
        >重置</el-button
        >
        <el-button type="primary" icon="Plus" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格数据 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="supplierName" label="供应商名称"></el-table-column>
      <el-table-column prop="address" label="供应商地址"></el-table-column>
      <el-table-column prop="linkUser" label="联系人"></el-table-column>
      <el-table-column prop="linkPhone" label="联系电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="postalCode" label="邮编"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status == '1'" type="danger" size="normal"  effect="dark">停止合作</el-tag>
          <el-tag v-if="scope.row.status == '0'" type="success" size="normal"  effect="dark">合作中</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
      <el-table-column width="220" align="center" label="操作">
        <template #default="scope">
          <el-button
              type="primary"
              icon="Edit"
              size="default"
              @click="editBtn(scope.row)"
          >编辑</el-button
          >
          <el-button
              type="danger"
              icon="Delete"
              size="default"
              @click="deleteBtn(scope.row)"
          >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page.sync="searchParm.currentPage"
        :page-sizes="[10, 20, 40, 80, 100]"
        :page-size="searchParm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="searchParm.total"
        background
    >
    </el-pagination>

    <!-- 弹框 -->
    <SysDialog
        :title="dialog.title"
        :height="dialog.height"
        :width="dialog.width"
        :visible="dialog.visible"
        @on-close="onClose"
        @on-confirm="commit"
    >
      <template v-slot:content>
        <el-form
            :model="addModel"
            ref="addRef"
            :rules="rules"
            label-width="100px"
            :inline="false"
            size="default"
        >
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="supplierName" label="供应商名称">
                <el-input v-model="addModel.supplierName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="address" label="供应商地址">
                <el-input v-model="addModel.address"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="linkUser" label="负责人">
                <el-input v-model="addModel.linkUser"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="linkPhone" label="负责人电话">
                <el-input v-model="addModel.linkPhone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="email" label="邮箱">
                <el-input v-model="addModel.email"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="postalCode" label="邮政编码">
                <el-input v-model="addModel.postalCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item prop="status" label="状态:">
            <el-radio-group v-model="addModel.status">
              <el-radio :label="'0'">启用</el-radio>
              <el-radio :label="'1'">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </template>
    </SysDialog>
  </el-main>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from "vue";
import { ElMessage, type FormInstance } from "element-plus";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import useInstance from "@/hooks/useInstance";
import {
  addApi,
  getListApi,
  editApi,
  deleteApi,
} from "@/api/supplier/index.ts";
import { type Supplier } from "@/api/supplier/index.ts";
const {global} = useInstance()
//表单的ref属性
const addRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onShow } = useDialog();
//搜索表单绑定对象
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  supplierName: "",
  linkUser: "",
  linkPhone: "",
  total: 0,
});
//新增表单绑定的对象
const addModel = reactive({
  supplierId: "",
  supplierName: "",
  address: "",
  linkUser: "",
  linkPhone: "",
  email: "",
  postalCode: "",
  status: "",
});
//表单验证规则
const rules = reactive({
  supplierName: [
    {
      required: true,
      message: "请填写供应商名称",
      trigger: ["blur", "change"],
    },
  ],
  address: [
    {
      required: true,
      message: "请填写供应商地址",
      trigger: ["blur", "change"],
    },
  ],
  linkUser: [
    { required: true, message: "请填写联系人", trigger: ["blur", "change"] },
  ],
  linkPhone: [
    { required: true, message: "请填写联系电话", trigger: ["blur", "change"] },
  ],
  status: [
    { required: true, message: "请选择状态", trigger: ["blur", "change"] },
  ],
});
//搜索按钮
const searchBtn = () => {
  getList();
};
//重置按钮
const resetBtn = () => {
  searchParm.currentPage = 1;
  searchParm.linkPhone = "";
  searchParm.linkUser = "";
  searchParm.supplierName = "";
  getList();
};
const tags = ref("");
//新增按钮
const addBtn = () => {
  tags.value = "0";
  //设置弹框的属性
  dialog.title = "新增供应商";
  dialog.height = 230;
  dialog.width = 650;
  //清空表单
  addRef.value?.resetFields();
  // 编辑过再点新增：supplierId 没绑表单项，resetFields 清不到，残留旧主键会导致新增主键冲突
  addModel.supplierId = "";
  //显示弹框
  onShow();
};
//编辑按钮
const editBtn = (row: Supplier) => {
  tags.value = "1";
  //设置弹框的属性
  dialog.title = "编辑供应商";
  dialog.height = 230;
  dialog.width = 650;
  //清空表单
  addRef.value?.resetFields();
  //显示弹框
  onShow();
  nextTick(() => {
    Object.assign(addModel, row);
  });
};
//删除按钮
const deleteBtn =async (row: Supplier) => {
  console.log(row);
  const confirm = await global.$myconfirm('确定删除该数据吗?')
  if(confirm){
    let res = await deleteApi(row.supplierId)
    //返回成功
    if (res && res.code == 200) {
      //信息提示
      ElMessage.success(res.msg);
      //刷新列表
      getList()
    }
  }
};
//表格数据
const tableList = ref([]);
//查询列表
const getList = async () => {
  let res = await getListApi(searchParm);
  if (res && res.code == 200) {
    console.log(res);
    tableList.value = res.data.records;
    searchParm.total = res.data.total;
  }
};
//页容量改变时触发
const sizeChange = (size: number) => {
  searchParm.pageSize = size;
  searchParm.currentPage = 1;
  getList();
};
//页数改变时触发
const currentChange = (page: number) => {
  searchParm.currentPage = page;
  getList();
};
//表单提交
const commit = () => {
  //表单验证
  addRef.value?.validate(async (valid) => {
    if (valid) {
      //调用新增接口
      let res = null;
      if (tags.value == "0") {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
      //返回成功
      if (res && res.code == 200) {
        //信息提示
        ElMessage.success(res.msg);
        //关闭弹框
        onClose();
        //刷新列表
        getList()
      }
    }
  });
};
//表格高度
const tableHeight = ref(0);
onMounted(() => {
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230;
  });
  getList();
});
</script>

<style scoped></style>
