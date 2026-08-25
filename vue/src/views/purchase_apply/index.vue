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
            placeholder="请输入供应商的名称"
            v-model="searchParm.supplierName"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
            placeholder="请输入商品名称"
            v-model="searchParm.goodsName"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" @click="searchBtn">搜索</el-button>
        <el-button type="danger" @click="resetBtn" plain icon="Close"
        >重置</el-button
        >
        <el-button icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格数据 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="goodsName" label="采购商品"></el-table-column>
      <el-table-column prop="supplierName" label="供应商"></el-table-column>
      <el-table-column prop="purchasePrice" label="采购单价"></el-table-column>
      <el-table-column prop="purchaseCount" label="采购数量"></el-table-column>
      <el-table-column prop="nickName" label="申请人"></el-table-column>
      <el-table-column prop="approveUser" label="审批人"></el-table-column>
      <el-table-column prop="status" label="审批状态">
        <template #default="scope">
          <el-tag
              v-if="scope.row.status == '0'"
              type="primary"
              size="default"
              effect="dark"
          >待审批</el-tag
          >
          <el-tag
              v-if="scope.row.status == '1'"
              type="danger"
              size="default"
              effect="dark"
          >已审批</el-tag
          >
          <el-tag
              v-if="scope.row.status == '2'"
              type="danger"
              size="default"
              effect="dark"
          >已拒绝</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" width="320" align="center">
        <template #default="scope">
          <el-button
              type="primary"
              icon="Edit"
              size="default"
              @click="editBtn(scope.row)"
          >编辑</el-button
          >
          <el-button
              type="warning"
              icon="Edit"
              size="default"
              @click="applyBtn(scope.row)"
          >审批</el-button
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
        :page-sizes="[20, 40, 80, 100]"
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
            label-width="80px"
            :inline="false"
            size="default"
        >
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="supplierId" label="供应商:">
                <el-select
                    @change="supChange"
                    style="width: 100%"
                    v-model="addModel.supplierId"
                    placeholder="请选择供应商"
                    size="default"
                >
                  <el-option
                      v-for="item in options"
                      :key="item['value']"
                      :label="item['label']"
                      :value="item['value']"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="goodsId" label="商品:">
                <el-select
                    style="width: 100%"
                    v-model="addModel.goodsId"
                    placeholder="请选择商品"
                    size="default"
                >
                  <el-option
                      v-for="item in goodsoptions"
                      :key="item['value']"
                      :label="item['label']"
                      :value="item['value']"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="purchasePrice" label="采购价格">
                <el-input v-model="addModel.purchasePrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="purchaseCount" label="采购数量">
                <el-input v-model="addModel.purchaseCount"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>
    </SysDialog>
  </el-main>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, nextTick } from "vue";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import {ElMessage, ElMessageBox, type FormInstance} from "element-plus";
import { selectListApi, selectGoodsListApi } from "@/api/goods/index.ts";
import { addApi, getListApi, type PurchaseApply,editApi,deleteApi,doApplyApi } from "@/api/purchase_apply/index.ts";
import { userStore } from "@/stores/user/index.ts";
import useInstance from "@/hooks/useInstance";

const {global}= useInstance()
const store = userStore();
//表单的ref属性
const addRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onShow } = useDialog();
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  goodsName: "",
  supplierName: "",
  total: 0,
});
//表单绑定对象
const addModel = reactive({
  applyId: "",
  supplierId: "",
  goodsId: "",
  purchasePrice: "",
  purchaseCount: "",
  applyUser: store.getUserId,
  approveUser: "",
  status:""
});
const supplierId = ref("");
//供应商选择事件
const supChange = (id: string) => {
  console.log(id);
  supplierId.value = id;
  addModel.goodsId = "";
  selectGoodsList();
};
//表单验证规则
const rules = reactive({
  supplierId: [
    {
      required: true,
      message: "请选择供应商",
      trigger: "change",
    },
  ],
  goodsId: [
    {
      required: true,
      message: "请选择商品",
      trigger: "change",
    },
  ],
  purchasePrice: [
    {
      required: true,
      message: "请输入采购价格",
      trigger: "change",
    },
  ],
  purchaseCount: [
    {
      required: true,
      message: "请输入采购数量",
      trigger: "change",
    },
  ],
});
//查询供应商数据
const options = ref([]);
const selectList = async () => {
  let res = await selectListApi();
  if (res && res.code == 200) {
    options.value = res.data;
  }
};
//商品数据
const goodsoptions = ref([]);
const selectGoodsList = async () => {
  let res = await selectGoodsListApi(supplierId.value);
  if (res && res.code == 200) {
    goodsoptions.value = res.data;
  }
};
//搜索
const searchBtn = () => {
  getList()
};
//重置
const resetBtn = () => {
  searchParm.currentPage = 1;
  searchParm.goodsName = ''
  searchParm.supplierName = ''
  getList()
};
const tags = ref("");
//新增
const addBtn = () => {
  tags.value = "0";
  // 编辑过再点新增：applyId 没绑表单项，resetFields 清不到，残留旧主键会导致新增主键冲突
  addModel.applyId = "";
  //设置弹框属性
  dialog.title = "新增采购申请";
  dialog.height = 180;
  selectList();
  onShow();
};
//编辑
const editBtn = (row:PurchaseApply)=>{
  //已审批的不能编辑
  if(row.status == '1'){
    ElMessage.warning('已审批通过，不能编辑!')
    return;
  }
  tags.value = "1";
  //设置弹框属性
  dialog.title = "编辑采购申请";
  dialog.height = 180;
  selectList();
  onShow();
  nextTick(()=>{
    Object.assign(addModel,row)
    addModel.status = '0'
  })
  //清空表单
  addRef.value?.resetFields();
}
//删除
const deleteBtn = async (row:PurchaseApply)=>{
  console.log(row)
  const confirm = await global.$myconfirm('确定删除该数据吗?')
  if(confirm){
    let res = await deleteApi(row.applyId)
    //返回成功
    if (res && res.code == 200) {
      //信息提示
      ElMessage.success(res.msg);
      //刷新列表
      getList()
    }
  }
}
//审批
const applyBtn = (row:PurchaseApply)=>{
//已审批的不能编辑
  if(row.status == '1'){
    ElMessage.warning('已审批通过，不能重复审批!')
    return;
  }
  ElMessageBox.prompt("", "采购审批", {
    distinguishCancelAndClose: true,
    confirmButtonText: "同意采购",
    cancelButtonText: "拒绝采购",
    inputPattern: /^.+$/,
    inputErrorMessage: "请填写审批备注",
    inputPlaceholder: "请填写审批备注",
    beforeClose: async (action, instance, done) => {
      console.log(action);
      if (action === "confirm") {
        //同意审批
        console.log(instance.inputValue);
        let res = await doApplyApi({
          applyId: row.applyId,
          type: "0",
          reason: instance.inputValue,
          approveUser:store.getNickName
        });
        if (res && res.code == 200) {
          getList();
          ElMessage.success(res.msg);
          done();
        }
      } else if (action === "cancel") {
        //拒绝审批
        let res = await doApplyApi({
          applyId: row.applyId,
          type: "1",
          reason: instance.inputValue,
          approveUser:store.getNickName
        });
        if (res && res.code == 200) {
          getList();
          ElMessage.success(res.msg);
          done();
        }
      }
      done();
    },
  });
}
//表格数据
const tableList = ref([]);
//列表查询
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
  addRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      let res = null;
      if (tags.value == "0") {
        res = await addApi(addModel);
      }else{
        res = await editApi(addModel)
      }
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        onClose();
        //刷新表格
        getList()
      }
    }
  });
};
//表格高度
const tableHeight = ref(0)
onMounted(() => {
  getList();
  nextTick(()=>{
    tableHeight.value = window.innerHeight - 230
  })
});
</script>

<style scoped></style>
