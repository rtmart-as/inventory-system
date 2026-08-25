<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="searchParm" :inline="true" size="default">
      <el-form-item>
        <el-input
            placeholder="请输入销售员"
            v-model="searchParm.nickName"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-input
            placeholder="请输入供应商名称"
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
        <el-button icon="Close" type="danger" plain @click="resetBtn"
        >重置</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格数据 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="returnId" label="订单号"></el-table-column>
      <el-table-column prop="goodsName" label="销售商品"></el-table-column>
      <el-table-column prop="supplierName" label="供应商"></el-table-column>
      <el-table-column prop="returnPrice" label="退货金额"></el-table-column>
      <el-table-column prop="returnCount" label="退货数量"></el-table-column>
      <el-table-column prop="nickName" label="销售员"></el-table-column>
      <el-table-column prop="status" label="是否审核">
        <template #default="scope">
          <el-tag
              v-if="scope.row.status == '0'"
              type="warning"
              size="default"
              effect="dark"
          >待审核</el-tag
          >
          <el-tag
              v-if="scope.row.status == '1'"
              type="primary"
              size="default"
              effect="dark"
          >已审核</el-tag
          >
          <el-tag
              v-if="scope.row.status == '2'"
              type="danger"
              size="default"
              effect="dark"
          >拒绝退货</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template #default="scope">
          <el-button
              v-if="scope.row.status == '0'"
              type="success"
              icon="Edit"
              size="default"
              @click="confirmBtn(scope.row)"
          >审批</el-button
          >
          <el-button
              v-if="scope.row.status == '0'"
              type="danger"
              icon="Edit"
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
  </el-main>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { getListApi, applyApi, deleteApi } from "@/api/sale_return/index";
import { userStore } from '@/stores/user/index'
import useInstance from "@/hooks/useInstance";
const { global } = useInstance();
const store = userStore()
//搜索绑定对象
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  goodsName: "",
  supplierName: "",
  nickName: "",
  total: 0,
  createUser:store.getUserId
});
//搜索按钮
const searchBtn = () => {
  getList();
};
//重置按钮
const resetBtn = () => {
  searchParm.currentPage = 1;
  searchParm.goodsName = "";
  searchParm.nickName = "";
  searchParm.supplierName = "";
  getList();
};
//页容量改变时触发
const sizeChange = (size: number) => {
  searchParm.pageSize = size;
  getList();
};
//页数改变时触发
const currentChange = (page: number) => {
  searchParm.currentPage = page;
  getList();
};
//表格高度
const tableHeight = ref(0);
//表格数据
const tableList = ref([]);
//查询列表
const getList = async () => {
  let res = await getListApi(searchParm);
  if (res && res.code == 200) {
    //设置表格数据
    console.log(res);
    tableList.value = res.data.records;
    //设置分页总条数
    searchParm.total = res.data.total;
  }
};
//退货审核
const confirmBtn = (row:any)=>{
  if (row.status == "1") {
    ElMessage.warning("已经退货，不要重复审批！");
    return;
  }
  console.log(row);
  ElMessageBox.prompt("", "退货审批", {
    distinguishCancelAndClose: true,
    confirmButtonText: "同意退货",
    cancelButtonText: "拒绝退货",
    inputPattern: /^.+$/,
    inputErrorMessage: "请填写审批备注",
    inputPlaceholder: "请填写审批备注",
    beforeClose: async (action, instance, done) => {
      console.log(action);
      if (action === "confirm") {
        //同意审批
        console.log(instance.inputValue);
        let res = await applyApi({
          returnId: row.returnId,
          type: "1",
          applyDesc: instance.inputValue,
          applyUser: store.getNickName,
        });
        if (res && res.code == 200) {
          getList();
          ElMessage.success(res.msg);
          done();
        }
      } else if (action === "cancel") {
        //拒绝审批
        let res = await applyApi({
          returnId: row.returnId,
          type: "2",
          applyDesc: instance.inputValue,
          applyUser: store.getNickName,
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
//删除
const deleteBtn = async (row:any)=>{
  console.log(row);
  if (row.status == "1") {
    ElMessage.warning("已经退货，不删除退货信息！");
    return;
  }
  const confirm = await global.$myconfirm("确定删除该数据吗?");
  if (confirm) {
    let res = await deleteApi(row.returnId);
    if (res && res.code == 200) {
      ElMessage.success(res.msg);
      getList();
    }
  }
}
onMounted(() => {
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230;
  });
  getList();
});
</script>

<style scoped></style>

