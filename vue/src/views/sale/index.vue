<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
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
        <el-button icon="Plus" type="primary" @click="addBtn"
        >录入订单</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格数据 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="goodsName" label="销售商品"></el-table-column>
      <el-table-column prop="supplierName" label="供应商"></el-table-column>
      <el-table-column prop="salePrice" label="销售金额"></el-table-column>
      <el-table-column prop="saleCount" label="销售数量"></el-table-column>
      <el-table-column prop="nickName" label="销售员"></el-table-column>
      <el-table-column prop="status" label="是否确定">
        <template #default="scope">
          <el-tag
              v-if="scope.row.status == '0'"
              type="warning"
              size="default"
              effect="dark"
          >待确定</el-tag
          >
          <el-tag
              v-if="scope.row.status == '1'"
              type="primary"
              size="default"
              effect="dark"
          >已确定</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column label="操作" width="320" align="center">
        <template #default="scope">
          <el-button
              v-if="scope.row.status == '0'"
              type="primary"
              icon="Edit"
              size="default"
              @click="editBtn(scope.row)"
          >编辑</el-button
          >
          <el-button
              v-if="scope.row.status == '0'"
              type="success"
              icon="Edit"
              size="default"
              @click="confirmBtn(scope.row)"
          >确认订单</el-button
          >
          <el-button
              v-if="scope.row.status == '1'"
              type="warning"
              icon="Edit"
              size="default"
              @click="returnBtn(scope.row)"
          >退货</el-button
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
    <!-- 新增弹框 -->
    <SysDialog
        :title="dialog.title"
        :width="dialog.width"
        :visible="dialog.visible"
        :height="dialog.height"
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
              <el-form-item prop="salePrice" label="销售金额">
                <el-input v-model="addModel.salePrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="saleCount" label="销售数量">
                <el-input v-model="addModel.saleCount"></el-input>
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
import { ElMessage, type FormInstance } from "element-plus";
import { selectListApi, selectGoodsListApi } from "@/api/goods/index.ts";
import {
  addApi,
  getListApi,
  type SaleOrder,
  editApi,
  deleteApi,
  confirmOrderApi,
} from "@/api/sale_order/index.ts";
import { userStore } from "@/stores/user/index.ts";
import useInstance from "@/hooks/useInstance.ts";
const store = userStore();
const { global } = useInstance();
//表单ref属性
const addRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onShow } = useDialog();
//搜索绑定对象
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  goodsName: "",
  supplierName: "",
  nickName: "",
  total: 0,
});
//表单绑定对象
const addModel = reactive({
  orderId: "",
  goodsId: "",
  supplierId: "",
  salePrice: "",
  saleCount: "",
  status: "0",
  saleUser: "",
});
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
  salePrice: [
    {
      required: true,
      message: "请输入销售金额",
      trigger: "change",
    },
  ],
  saleCount: [
    {
      required: true,
      message: "请输入销售数量",
      trigger: "change",
    },
  ],
});
const supplierId = ref("");
//查询供应商数据
const options = ref([]);
const selectList = async () => {
  let res = await selectListApi();
  if (res && res.code == 200) {
    options.value = res.data;
  }
};
const supChange = (id: string) => {
  console.log(id);
  supplierId.value = id;
  addModel.goodsId = "";
  selectGoodsList();
};
//商品数据
const goodsoptions = ref([]);
const selectGoodsList = async () => {
  let res = await selectGoodsListApi(supplierId.value);
  if (res && res.code == 200) {
    goodsoptions.value = res.data;
  }
};
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
const tags = ref("");
//新增按钮
const addBtn = () => {
  tags.value = "0";
  addRef.value?.resetFields();
  dialog.title = "录入订单信息";
  dialog.height = 180;
  onShow();
  //查询供应商数据
  selectList();
  addModel.saleUser = store.getUserId;
};
//编辑
const editBtn = (row: SaleOrder) => {
  tags.value = "1";
  addRef.value?.resetFields();
  dialog.title = "编辑订单信息";
  dialog.height = 180;
  onShow();
  //查询供应商数据
  selectList();
  nextTick(() => {
    Object.assign(addModel, row);
  });
};
//删除
const deleteBtn = async (row: SaleOrder) => {
  console.log(row);
  const confirm = await global.$myconfirm("确定删除该数据吗？");
  if (confirm) {
    let res = await deleteApi(row.orderId);
    if (res && res.code == 200) {
      getList();
      ElMessage.success(res.msg);
    }
  }
};
//确定订单
const confirmBtn = async (row: SaleOrder) => {
  console.log(row);
  const confirm = await global.$myconfirm(
      "请核对单信息，确定之后，信息不能编辑、删除！"
  );
  if (confirm) {
    let res = await confirmOrderApi(row);
    if (res && res.code == 200) {
      getList();
      ElMessage.success(res.msg);
    }
  }
};
//销售退货
const returnBtn = (row: SaleOrder) => {
  console.log(row);
};
//提交
const commit = async () => {
  addRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if (tags.value == "0") {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
      if (res && res.code == 200) {
        getList();
        ElMessage.success(res.msg);
        onClose();
      }
    }
  });
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

<style scoped></style>
