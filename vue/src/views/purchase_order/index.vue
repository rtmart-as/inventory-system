<template>
  <el-main height="">
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
      <el-table-column prop="goodsName" label="采购商品"></el-table-column>
      <el-table-column prop="supplierName" label="供应商"></el-table-column>
      <el-table-column prop="purchasePrice" label="采购单价"></el-table-column>
      <el-table-column prop="purchaseCount" label="采购数量"></el-table-column>
      <el-table-column prop="batchNum" label="采购批号"></el-table-column>
      <el-table-column prop="purchaseUser" label="入库验货人"></el-table-column>
      <el-table-column prop="status" label="是否入库">
        <template #default="scope">
          <el-tag
              v-if="scope.row.status == '0'"
              type="warning"
              size="default"
              effect="dark"
          >未入库</el-tag
          >
          <el-tag
              v-if="scope.row.status == '1'"
              type="primary"
              size="default"
              effect="dark"
          >已入库</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column prop="payStatus" label="支付状态">
        <template #default="scope">
          <el-tag
              v-if="scope.row.payStatus != '1'"
              type="danger"
              size="default"
              effect="dark"
          >未支付</el-tag
          >
          <el-tag
              v-if="scope.row.payStatus == '1'"
              type="success"
              size="default"
              effect="dark"
          >已支付</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" width="430" align="center">
        <template #default="scope">
          <el-button
              v-if="scope.row.payStatus != '1'"
              type="success"
              icon="Money"
              size="default"
              @click="payBtn(scope.row)"
          >付款</el-button
          >
          <el-button
              type="primary"
              icon="Edit"
              size="default"
              @click="intoBtn(scope.row)"
          >验货入库</el-button
          >
          <el-button
              type="warning"
              icon="Edit"
              size="default"
              @click="returnBtn(scope.row)"
          >退货</el-button
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
    <!-- 退货框 -->
    <el-dialog width="620px" v-model="dialogFormVisible" title="退货信息">
      <el-form :rules="returnRules" ref="returnRef" :model="returnModel">
        <el-form-item prop="batchNum" label="产品批号">
          <el-input v-model="returnModel.batchNum" />
        </el-form-item>
        <el-form-item prop="returnPrice" label="退货金额">
          <el-input v-model="returnModel.returnPrice" />
        </el-form-item>
        <el-form-item prop="returnCount" label="退货数量">
          <el-input v-model="returnModel.returnCount" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消退货</el-button>
          <el-button type="primary" @click="returnCommit"> 确定退货 </el-button>
        </span>
      </template>
    </el-dialog>
  </el-main>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted, nextTick } from "vue";
import { ElMessage, ElMessageBox, type FormInstance } from "element-plus";
import { getListApi, enterStockApi, returnStockApi } from "@/api/purchase_order/index";
import { userStore } from "@/stores/user/index";
import { createPayApi } from "@/api/pay/index";
import usePayPolling from "@/hooks/usePayPolling";
const store = userStore();
const dialogFormVisible = ref(false);
const returnRef = ref<FormInstance>();
//退货表单
const returnModel = reactive({
  orderId: "",
  returnPrice: "",
  returnCount: "",
  batchNum: "",
  createUser: "",
});
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
//入库验货
const intoBtn = (row:any)=>{
  console.log(row)
  if (row.status === "1") {
    ElMessage.warning("该商品已经入库，不要重复入库");
    return;
  }
  ElMessageBox.prompt("", "验货入库", {
    // distinguishCancelAndClose: true,
    confirmButtonText: "确定入库",
    cancelButtonText: "取消入库",
    inputPattern: /^.+$/,
    inputErrorMessage: "请填写商品批号",
    inputPlaceholder: "请填写商品批号",
    beforeClose: async (action, instance, done) => {
      console.log(action);
      if (action === "confirm") {
        //确定入库
        console.log(instance.inputValue);
        let res = await enterStockApi({
          orderId: row.orderId,
          batchNum: instance.inputValue,
          purchaseUser: store.getNickName,
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
//付款(支付宝收银台)
const { start: startPayPolling } = usePayPolling();
const payBtn = async (row:any)=>{
  //先开空窗口再赋值,规避浏览器弹窗拦截
  const win = window.open("", "_blank");
  try {
    let res = await createPayApi({ bizType: "2", orderId: row.orderId });
    if (res && res.code == 200 && res.data && res.data.url) {
      if (win) {
        win.location.href = res.data.url;
      } else {
        //窗口被拦截时当前页跳转
        window.location.href = res.data.url;
      }
      //支付完成后刷新列表
      startPayPolling("2", row.orderId, getList);
    } else {
      win?.close();
    }
  } catch (e) {
    win?.close();
  }
}
//退货
const returnBtn = (row:any)=>{
  //清空表单
  returnRef.value?.resetFields();
  returnModel.batchNum = row.batchNum;
  returnModel.orderId = row.orderId;
  returnModel.createUser = store.getNickName;
  dialogFormVisible.value = true;
};
//退货表单验证
const returnRules = reactive({
  batchNum: [
    {
      required: true,
      message: "请输入产品批号",
      trigger: "change",
    },
  ],
  returnPrice: [
    {
      required: true,
      message: "请输入退货金额",
      trigger: "change",
    },
  ],
  returnCount: [
    {
      required: true,
      message: "请输入退货数量",
      trigger: "change",
    },
  ]
});
//退货提交
const returnCommit = () => {
  returnRef.value?.validate(async (valid) => {
    if (valid) {
      let res = await returnStockApi(returnModel);
      if (res && res.code == 200) {
        getList()
        ElMessage.success(res.msg);
        dialogFormVisible.value = false;
      }
    }
  });
}
onMounted(() => {
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230;
  });
  getList();
});
</script>

<style scoped></style>
