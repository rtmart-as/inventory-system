<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
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
        <el-input
            placeholder="请输入商品编码"
            v-model="searchParm.goodsCode"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" @click="searchBtn">搜索</el-button>
        <el-button type="danger" plain icon="Close" @click="resetBtn"
        >重置</el-button
        >
        <el-button icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格数据 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="image" label="商品图片" align="center">
        <template #default="scope">
          <el-image
              v-if="scope.row.image"
              style="width: 100px; height: 60px"
              :src="imgbase + scope.row.image.split(',')[0]"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="goodsName" label="商品名称"></el-table-column>
      <el-table-column prop="supplierName" label="供应商"></el-table-column>
      <el-table-column prop="goodsCode" label="商品编号"></el-table-column>
      <el-table-column prop="place" label="产地"></el-table-column>
      <el-table-column prop="salePrice" label="销售价格"></el-table-column>
      <el-table-column prop="stock" label="库存数量"></el-table-column>
      <el-table-column prop="stockWarn" label="库存预警"></el-table-column>
      <el-table-column prop="speci" label="规格"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status == '0'" type="success" size="default"
          >在售</el-tag
          >
          <el-tag v-if="scope.row.status == '1'" type="danger" size="default"
          >停售</el-tag
          >
        </template>
      </el-table-column>
      <!-- <el-table-column prop="goodsDesc" label="描述"></el-table-column> -->
      <el-table-column prop="orderNum" label="序号"></el-table-column>
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

    <SysDialog
        :title="dialog.title"
        :width="dialog.width"
        :height="dialog.height"
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
              <el-form-item prop="goodsName" label="商品名称:">
                <el-input v-model="addModel.goodsName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="goodsCode" label="商品编号:">
                <el-input v-model="addModel.goodsCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="supplierId" label="供应商:">
                <el-select
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
              <el-form-item prop="place" label="产地:">
                <el-input v-model="addModel.place"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="salePrice" label="销售价格:">
                <el-input v-model="addModel.salePrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="stock" label="库存数量:">
                <el-input v-model="addModel.stock"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="stockWarn" label="库存预警:">
                <el-input v-model="addModel.stockWarn"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" :offset="0">
              <el-form-item prop="speci" label="规格:">
                <el-input v-model="addModel.speci"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="orderNum" label="序号:">
                <el-input v-model="addModel.orderNum"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12" :offset="0">
              <el-form-item prop="status" label="状态:">
                <el-radio-group v-model="addModel.status">
                  <el-radio :value="'0'">在售</el-radio>
                  <el-radio :value="'1'">停售</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item prop="image" label="商品图片:">
            <UploadImage
                @getImg="getImg"
                ref="uploadRef"
                :fileList="fileList"
                :oldUrl="oldUrl"
            ></UploadImage>
          </el-form-item>
        </el-form>
      </template>
    </SysDialog>
  </el-main>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, computed, nextTick } from "vue";
import SysDialog from "@/components/SysDialog.vue";
import UploadImage from "@/components/UploadImage.vue";
import useDialog from "@/hooks/useDialog";
import { ElMessage, type FormInstance, type UploadUserFile } from "element-plus";
import { type NewType } from "@/type/BaseType";
import { addApi, selectListApi, getListApi, editApi, deleteApi } from "@/api/goods/index.ts";
import { type Goods } from "@/api/goods/index.ts";
import useInstance from "@/hooks/useInstance";

const {global} = useInstance()

const imgbase = computed(() => {
  return import.meta.env.VITE_BASE_API_IMG;
});
const addRef = ref<FormInstance>();
//图片上传ref属性
const uploadRef = ref();
//图片回显的数据
const fileList = ref<Array<UploadUserFile>>([]);
//图片默认的url
const oldUrl = ref<Array<{ url: string }>>([]);
const imgUrl = ref<Array<{ url: string }>>([]);

const { dialog, onClose, onShow } = useDialog();
//搜索参数
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  goodsName: "",
  supplierName: "",
  goodsCode: "",
  total: 0,
});
//表单绑定对象
const addModel = reactive({
  goodsId: "",
  goodsName: "",
  supplierId: "",
  place: "",
  goodsDesc: "",
  salePrice: "",
  stock: "",
  stockWarn: "",
  image: "",
  speci: "",
  status: "",
  orderNum: "",
  goodsCode: "",
});
//图片上传返回: /images/11.png,/images/22.png
const getImg = (img: NewType) => {
  console.log(img);
  imgUrl.value = oldUrl.value.concat(img.newImgUrl);
  if (img.deleteUrl.length > 0) {
    //删除数据
    let newArr = imgUrl.value.filter(
        (x) => !img.deleteUrl.some((item) => x.url == item.url)
    );
    imgUrl.value = newArr;
  }
  let url = "";
  //图片地址转为逗号分隔的字符串  aa.png,bb.png,
  for (let k = 0; k < imgUrl.value.length; k++) {
    const item = imgUrl.value[k];
    if (item) {
      url = url + item.url + ",";
    }
  }
  //去掉字符串最后一个逗号
  addModel.image = url.substring(0, url.lastIndexOf(","));
};
//查询供应商下拉数据
const options = ref([]);
const selectList = async () => {
  let res = await selectListApi();
  if (res && res.code == 200) {
    options.value = res.data;
  }
};
//表单验证规则
const rules = reactive({
  goodsName: [
    { required: true, message: "请填写商品名称", trigger: ["blur", "change"] },
  ],
  goodsCode: [
    { required: true, message: "请填写商品编号", trigger: ["blur", "change"] },
  ],
  supplierId: [
    { required: true, message: "请选择供应商", trigger: ["blur", "change"] },
  ],
  place: [
    { required: true, message: "请填写产地", trigger: ["blur", "change"] },
  ],
  salePrice: [
    { required: true, message: "请填写销售价格", trigger: ["blur", "change"] },
  ],
  stock: [
    { required: true, message: "请填写库存量", trigger: ["blur", "change"] },
  ],
  stockWarn: [
    { required: true, message: "请填写库存预警", trigger: ["blur", "change"] },
  ],
  speci: [
    { required: true, message: "请填写规格", trigger: ["blur", "change"] },
  ],
  status: [
    { required: true, message: "请选择状态", trigger: ["blur", "change"] },
  ],
  image: [
    { required: true, message: "请上传图片", trigger: ["blur", "change"] },
  ],
});
//搜索
const searchBtn = () => {
  getList();
};
//重置
const resetBtn = () => {
  searchParm.goodsCode = "";
  searchParm.goodsName = "";
  searchParm.supplierName = "";
  searchParm.currentPage = 1;
  getList();
};
const tags = ref("");
//新增
const addBtn = async () => {
  addModel.goodsId = "";
  tags.value = "0";
  await selectList();
  console.log("11");
  //设置弹框属性
  dialog.title = "新增商品";
  dialog.height = 450;
  dialog.width = 860;
  onShow();
  //清空表单
  addRef.value?.resetFields();
};
//编辑
const editBtn = async (row: Goods) => {
  addModel.goodsId = "";
  imgUrl.value = [];
  oldUrl.value = [];
  fileList.value = [];
  tags.value = "1";
  await selectList();
  console.log("11");
  //设置弹框属性
  dialog.title = "编辑商品";
  dialog.height = 450;
  dialog.width = 860;
  onShow();
  nextTick(() => {
    Object.assign(addModel, row);
    //图片回显
    if (addModel.image) {
      //逗号转换为数组  11.png，22.png
      let imgs = addModel.image.split(",");
      for (let i = 0; i < imgs.length; i++) {
        const item = imgs[i];
        if (item) {
          let img = { name: "", url: "" };
          img.name = item;
          img.url = imgbase.value + item;
          fileList.value.push(img);
          oldUrl.value.push({ url: item });
        }
      }
    }
  });
  //清空表单
  addRef.value?.resetFields();
};

//删除按钮
const deleteBtn =async (row: Goods) => {
  console.log(row);
  const confirm = await global.$myconfirm('确定删除该数据吗?')
  if(confirm){
    let res = await deleteApi(row.goodsId)
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
    tableList.value = res.data.records;
    searchParm.total = res.data.total;
  }
};
//页容量改变触发
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
  addRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if (tags.value == "0") {
        res = await addApi(addModel);
      }else{
        res = await editApi(addModel)
      }
      if (res && res.code == 200) {
        uploadRef.value.clearimg();
        ElMessage.success(res.msg);
        onClose();
        getList()
      }
    }
  });
};
//表格高度
const tableHeight = ref(0);
onMounted(() => {
  getList();
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230;
  });
});
</script>

<style scoped></style>
