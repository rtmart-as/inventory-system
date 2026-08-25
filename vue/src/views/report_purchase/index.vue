<template>
  <el-main>
    <el-form :model="searchParm" :inline="true" size="default">
      <el-form-item>
        <el-date-picker
            v-model="searchParm.startTime"
            type="month"
            value-format="YYYY-MM"
            placeholder="请选择开始时间"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
            v-model="searchParm.endTime"
            type="month"
            value-format="YYYY-MM"
            placeholder="请选择结束时间"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" @click="searchBtn">搜索</el-button>
        <el-button icon="Close" type="danger" plain @click="resetBtn"
        >重置</el-button
        >
        <el-button
            icon="Plus"
            type="success"
            @click="exportBtn"
        >导出</el-button
        >
      </el-form-item>
    </el-form>
    <el-table :data="tableList" border stripe>
      <el-table-column prop="goodsName" label="采购商品"></el-table-column>
      <el-table-column prop="purchasePrice" label="采购总价"></el-table-column>
      <el-table-column prop="purchaseCount" label="采购数量"></el-table-column>
      <el-table-column prop="purchaseMonth" label="采购年月"></el-table-column>
    </el-table>

  </el-main>

</template>

<script setup lang="ts">
import {getPurchaseTotalApi} from '@/api/purchase_order/index.ts'
import { onMounted, reactive, ref } from 'vue';
import {userStore} from '@/stores/user/index'
const store = userStore()
//列表参数
const searchParm = reactive({
  currentPage:1,
  pageSize:10,
  startTime:'',
  endTime:'',
  total:0
})
//定义表格数据
const tableList = ref([])
const getPurchaseTotal = async()=>{
  let res = await getPurchaseTotalApi(searchParm)
  if(res && res.code == 200){
    tableList.value = res.data;
  }
}
//搜索按钮
const searchBtn = ()=>{
  getPurchaseTotal()
}
//重置按钮
const resetBtn = ()=>{
  searchParm.currentPage = 1;
  searchParm.startTime = "";
  searchParm.endTime = "";
  getPurchaseTotal()
}
//导出
const exportBtn = () => {
  const abtn = document.createElement("a");
  abtn.href = import.meta.env.VITE_BASE_API + "/api/purchaseOrder/exportOrder?token="+store.getToken+"&currentPage="+searchParm.currentPage
      +"&pageSize="+searchParm.pageSize + "&startTime="+searchParm.startTime + "&endTime=" +searchParm.endTime;
  abtn.click();
};

onMounted(()=>{
  getPurchaseTotal()
})
</script>

<style scoped>

</style>
