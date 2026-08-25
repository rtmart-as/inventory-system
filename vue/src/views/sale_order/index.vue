<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <el-main>
    <!--搜索栏 -->
    <el-form :model="searchParm" :inline="true" size="default">
      <el-form-item label="">
        <el-input
            placeholder="请录入销售人姓名"
            v-model="searchParm.nickName"
        ></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-input
            placeholder="请录入供应商姓名"
            v-model="searchParm.supplierName"
        ></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-input
            placeholder="请录入商品名称"
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
      <el-table-column
          width="190"
          prop="orderId"
          label="订单号"
      ></el-table-column>
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
      <el-table-column label="操作" width="480" align="center">
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
              v-if="scope.row.status == '1' && scope.row.payStatus != '1'"
              type="success"
              icon="Money"
              size="default"
              @click="payBtn(scope.row)"
          >收款</el-button
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

    <!-- 弹框 -->
    <SysDialog
        :title="dialog.title"
        :width="dialog.width"
        :visible="dialog.visible"
        :height="dialog.height"
        @on-close="onClose"
        @on-confirm="commit"
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
    <!-- 退货弹框 -->
    <el-dialog
        v-model="dialogFormVisible"
        title="退货信息"
        width="620px"
    >
      <el-form :model="returnModel" ref="returnRef" :rules="returnRules" label-width="80px" :inline="false" size="default">
        <el-form-item prop="goodsName" label="退货商品">
          <el-input readonly v-model="returnModel.goodsName"></el-input>
        </el-form-item>
        <el-form-item prop="returnPrice" label="退货金额">
          <el-input v-model="returnModel.returnPrice"></el-input>
        </el-form-item>
        <el-form-item prop="returnCount" label="退货数量">
          <el-input v-model="returnModel.returnCount"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="退货备注">
          <el-input v-model="returnModel.description"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消退货</el-button>
          <el-button type="primary" @click="returnCommit">
            确定退货
          </el-button>
        </div>
      </template>
    </el-dialog>
  </el-main>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from "vue";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { ElMessage, type FormInstance } from "element-plus";
import { selectListApi, selectGoodsListApi } from "@/api/goods/index.ts";
import { userStore } from "@/stores/user/index.ts";
import {
  addApi,
  getListApi,
  type SaleOrder,
  editApi,
  deleteApi,
  confirmOrderApi,
} from "@/api/sale_order/index.ts";
import useInstance from "@/hooks/useInstance";
import { applyReturnApi } from "@/api/sale_return/index.ts";
import { createPayApi } from "@/api/pay/index.ts";
import usePayPolling from "@/hooks/usePayPolling";

const { global } = useInstance();
const ustore = userStore();
const returnRef = ref<FormInstance>()
//表单ref属性
const addRef = ref<FormInstance>();
//退货弹框属性
const dialogFormVisible = ref(false)
//退货表单绑定的对象
const returnModel = reactive({
  orderId: "",
  goodsId: "",
  goodsName:"",
  returnPrice: "",
  returnCount: "",
  createUser: "",
  description:"",
  status:"0"
})
//退货表单验证
const returnRules = reactive({
  goodsName: [
    {
      required: true,
      message: "请输入退货商品",
      trigger: "change",
    },
  ],
  description: [
    {
      required: true,
      message: "请输入退货描述、备注",
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
//弹框属性
const { dialog, onClose, onShow } = useDialog();
//搜索表单绑定的对象
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  goodsName: "",
  nickName: "",
  supplierName: "",
  total: 0,
  userId: ustore.getUserId,
});
//表单绑定对象
const addModel = reactive({
  orderId: "",
  goodsId: "",
  goodsName:"",
  supplierId: "",
  salePrice: "",
  saleCount: "",
  status: "0",
  saleUser: "",
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
//商品数据
const goodsoptions = ref([]);
const selectGoodsList = async () => {
  let res = await selectGoodsListApi(supplierId.value);
  if (res && res.code == 200) {
    goodsoptions.value = res.data;
  }
};
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
const tags = ref("");
//录入订单
const addBtn = () => {
  tags.value = "0";
  addRef.value?.resetFields();
  // 编辑过再点新增：resetFields 只重置绑了 prop 的表单项，主键不会清，残留旧主键会导致新增主键冲突
  addModel.orderId = "";
  dialog.title = "新增销售订单";
  dialog.height = 180;
  onShow();
  //查询供应商数据
  selectList();
  addModel.saleUser = ustore.getUserId;
};

//表格数据
const tableList = ref([]);
//获取表格数据
const getList = async () => {
  let res = await getListApi(searchParm);
  if (res && res.code == 200) {
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

//编辑
const editBtn = (row: SaleOrder) => {
  tags.value = "1";
  addRef.value?.resetFields();
  dialog.title = "编辑销售订单";
  dialog.height = 180;
  onShow();
  //查询供应商数据
  selectList();
  nextTick(() => {
    Object.assign(addModel, row);
    // addModel.saleUser = ustore.getUserId
  });
};

//确定
const confirmBtn = async (row: SaleOrder) => {
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
//退货
const returnBtn = (row: SaleOrder) => {
  returnModel.createUser = ustore.getUserId
  returnModel.orderId = row.orderId;
  returnModel.goodsId = row.goodsId
  returnModel.goodsName = row.goodsName as string;
  dialogFormVisible.value = true;
};
//退货提交
const returnCommit = ()=>{
  returnRef.value?.validate(async(valid)=>{
    if(valid){
      let res = await applyReturnApi(returnModel)
      if(res && res.code == 200){
        getList()
        dialogFormVisible.value = false;
        global.$myconfirm('退货申请已经提交，请在【销售退货】里面查看信息！')
      }
    }
  })
}
//搜索
const searchBtn = () => {
  getList();
};
//重置
const resetBtn = () => {
  searchParm.currentPage = 1;
  searchParm.goodsName = "";
  searchParm.nickName = "";
  searchParm.supplierName = "";
  getList();
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
//收款(支付宝收银台)
const { start: startPayPolling } = usePayPolling();
const payBtn = async (row: SaleOrder) => {
  //先开空窗口再赋值,规避浏览器弹窗拦截
  const win = window.open("", "_blank");
  try {
    let res = await createPayApi({ bizType: "1", orderId: row.orderId });
    if (res && res.code == 200 && res.data && res.data.url) {
      if (win) {
        win.location.href = res.data.url;
      } else {
        //窗口被拦截时当前页跳转
        window.location.href = res.data.url;
      }
      //支付完成后刷新列表
      startPayPolling("1", row.orderId, getList);
    } else {
      win?.close();
    }
  } catch (e) {
    win?.close();
  }
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
        onClose();
        getList();
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
