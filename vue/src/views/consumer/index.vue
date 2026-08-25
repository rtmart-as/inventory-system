<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
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
            placeholder="请输入客户名称"
            v-model="searchParm.consumerName"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
            placeholder="请输入联系人"
            v-model="searchParm.linkUser"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
            placeholder="请输入联系电话"
            v-model="searchParm.linkPhone"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" @click="searchBtn">搜索</el-button>
        <el-button icon="Close" type="danger" plain @click="resetBtn"
        >重置
        </el-button
        >
        <el-button icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格数据 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="consumerName" label="客户名称"></el-table-column>
      <el-table-column prop="address" label="客户地址"></el-table-column>
      <el-table-column prop="linkUser" label="联系人"></el-table-column>
      <el-table-column prop="linkPhone" label="联系电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="postalCode" label="邮编"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status == '0'" type="success" size="default"
          >合作中
          </el-tag
          >
          <el-tag v-if="scope.row.status == '1'" type="danger" size="default"
          >停止合作
          </el-tag
          >
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
          >编辑
          </el-button
          >
          <el-button
              type="danger"
              icon="Delete"
              size="default"
              @click="deleteBtn(scope.row)"
          >删除
          </el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page.sync="searchParm.currentPage"
        :page-sizes="[10,20, 40, 80, 100]"
        :page-size="searchParm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="searchParm.total" background>
    </el-pagination>
    <!-- 弹框 -->
    <SysDialog
        :title="dialog.title"
        :width="dialog.width"
        :height="dialog.height"
        :visible="dialog.visible"
        @onClose="onClose"
        @onConfirm="commit"
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
              <el-form-item prop="consumerName" label="客户名称:">
                <el-input v-model="addModel.consumerName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="address" label="客户地址:">
                <el-input v-model="addModel.address"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="linkUser" label="联系人:">
                <el-input v-model="addModel.linkUser"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="linkPhone" label="联系电话:">
                <el-input v-model="addModel.linkPhone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="email" label="邮箱:">
                <el-input v-model="addModel.email"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="postalCode" label="邮政编码:">
                <el-input v-model="addModel.postalCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="status" label="状态:">
                <el-radio-group v-model="addModel.status">
                  <el-radio :label="'0'">合作</el-radio>
                  <el-radio :label="'1'">停止合作</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>
    </SysDialog>
  </el-main>
</template>

<script setup lang="ts">
import {nextTick, onMounted, reactive, ref} from "vue";
import {ElMessage, type FormInstance} from "element-plus";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog.ts";
import {addApi, type Consumer, getListApi, editApi, deleteApi,} from '@/api/consumer/index.ts'
import useInstance from "@/hooks/useInstance";
const {global} = useInstance()
//表单ref属性
const addRef = ref<FormInstance>();
//弹框属性
const {dialog, onClose, onShow} = useDialog();
//搜索表单绑定对象
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  consumerName: "",
  linkUser: "",
  linkPhone: "",
  total: 0,
});

//新增表单绑定对象
const addModel = reactive({
  consumerId: "",
  consumerName: "",
  address: "",
  linkUser: "",
  linkPhone: "",
  email: "",
  postalCode: "",
  status: "",
});
//表单验证规则
const rules = reactive({
  consumerName: [
    {
      required: true,
      message: "请填写客户名称",
      trigger: ["blur", "change"],
    },
  ],
  address: [
    {
      required: true,
      message: "请填写客户地址",
      trigger: ["blur", "change"],
    },
  ],
  linkUser: [
    {required: true, message: "请填写联系人", trigger: ["blur", "change"]},
  ],
  linkPhone: [
    {required: true, message: "请填写联系电话", trigger: ["blur", "change"]},
  ],
  status: [
    {required: true, message: "请选择状态", trigger: ["blur", "change"]},
  ],
});
//搜索按钮
const searchBtn = () => {
  getList()
};
//重置按钮
const resetBtn = () => {
  searchParm.consumerName = ''
  searchParm.linkPhone = ''
  searchParm.linkUser = ''
  searchParm.currentPage = 1;
  getList()
};
const tags = ref("");
//新增按钮
const addBtn = () => {
  addModel.consumerId = ""
  tags.value = "0";
  //清空表单
  addRef.value?.resetFields();
  //点击新增按钮，显示出弹框
  dialog.title = "新增客户";
  onShow();
};
//表单提交
const commit = () => {
  addRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if (tags.value == "0") {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        //刷新列表
        getList();
        onClose();
      }
    }
  })
}
//表格数据
const tableList = ref([])
//获取表格数据
const getList = async () => {
  let res = await getListApi(searchParm)
  if (res && res.code == 200) {
    tableList.value = res.data.records;
    searchParm.total = res.data.total;
  }
}
//编辑
const editBtn = (row: Consumer) => {
  addModel.consumerId = ""
  tags.value = "1";
  //清空表单
  addRef.value?.resetFields();
  //点击新增按钮，显示出弹框
  dialog.title = "编辑客户";
  nextTick(() => {
    Object.assign(addModel, row);
  });
  onShow();
}
//删除
const deleteBtn = async (row: Consumer) => {
  const confirm = await global.$myconfirm('确定删除该数据吗?')
  if(confirm){
    let res = await deleteApi(row)
    if(res && res.code == 200){
      ElMessage.success(res.msg)
      getList()
    }
  }
}
//页容量改变触发
const sizeChange = (size: number) => {
  searchParm.pageSize = size;
  searchParm.currentPage = 1
  getList()
}
//页数改变时触发
const currentChange = (page: number) => {
  searchParm.currentPage = page;
  getList()
}
//表格高度
const tableHeight = ref(0)
onMounted(() => {
  getList()
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230
  })
})
</script>

<style scoped></style>
