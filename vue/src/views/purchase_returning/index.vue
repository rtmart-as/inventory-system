<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="searchParm" :inline="true" size="default">
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
      <el-table-column prop="goodsName" label="退货商品"></el-table-column>
      <el-table-column prop="supplierName" label="供应商"></el-table-column>
      <el-table-column prop="returnPrice" label="退货金额"></el-table-column>
      <el-table-column prop="returnCount" label="退货数量"></el-table-column>
      <el-table-column prop="batchNum" label="采购批号"></el-table-column>
      <el-table-column prop="createUser" label="退货人"></el-table-column>
      <el-table-column prop="createTime" label="退货时间"></el-table-column>
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
import {
  getListApi
} from "@/api/purchase_returning/index.ts";
//搜索绑定对象
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  goodsName: "",
  supplierName: "",
  total: 0,
});
//搜索按钮
const searchBtn = () => {
  getList();
};
//重置按钮
const resetBtn = () => {
  searchParm.goodsName = "";
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
onMounted(() => {
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230;
  });
  getList();
});
</script>

<style scoped>

</style>
