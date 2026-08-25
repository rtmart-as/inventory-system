<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="searchParm" :inline="true" size="default">
      <el-form-item>
        <el-input
            placeholder="请输入关键字"
            v-model="searchParm.keywords"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="Search" @click="searchBtn">搜索</el-button>
        <el-button icon="Close" type="danger" plain @click="resetBtn"
        >重置</el-button
        >
        <el-button
            v-permission="['sys:notice:add']"
            icon="Plus"
            type="primary"
            @click="addBtn"
        >新增</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 表格数据 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="noticeText" label="内容" />
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作" width="220" align="center">
        <template #default="scope">
          <el-button
              v-permission="['sys:notice:edit']"
              type="primary"
              icon="Edit"
              size="default"
              @click="editBtn(scope.row)"
          >编辑</el-button
          >
          <el-button
              v-permission="['sys:notice:delete']"
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
    <!-- 新增、编辑弹框 -->
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
          <el-form-item prop="title" label="标题">
            <el-input v-model="addModel.title" />
          </el-form-item>
          <el-form-item prop="noticeText" label="内容">
            <el-input v-model="addModel.noticeText" />
          </el-form-item>
        </el-form>
      </template>
    </SysDialog>
  </el-main>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from "vue";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog.ts";
import {
  addApi,
  editApi,
  deleteApi,
  getListApi,
  type SysNotice,
} from "@/api/sys_notice/index.ts";
import { ElMessage, type FormInstance } from "element-plus";
import useInstance from "@/hooks/useInstance.ts";
const { global } = useInstance();
//表单ref属性
const addRef = ref<FormInstance>();
const { dialog, onClose, onShow } = useDialog();
//列表查询参数
const searchParm = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
  keywords: "",
});
//表单绑定对象
const addModel = reactive({
  noticeId: "",
  title: "",
  noticeText: "",
});
//表单验证规则
const rules = reactive({
  noticeId: [
    { required: true, message: "不能为空", trigger: ["blur", "change"] },
  ],
  title: [{ required: true, message: "不能为空", trigger: ["blur", "change"] }],
  noticeText: [
    { required: true, message: "不能为空", trigger: ["blur", "change"] },
  ],
});
//搜索按钮
const searchBtn = () => {
  searchParm.currentPage = 1;
  getList();
};
//重置按钮
const resetBtn = () => {
  searchParm.currentPage = 1;
  searchParm.keywords=''
  getList();
};
const tags = ref("");
//新增
const addBtn = () => {
  addRef.value?.resetFields();
  // 编辑过再点新增时，resetFields 只重置绑了 prop 的表单项，
  // noticeId 没绑 el-form-item 不会被清空，会残留被编辑行的旧主键，
  // 新增 POST 携带非空主键会插入重复主键导致 500，这里手动清空
  addModel.noticeId = "";
  tags.value = "0";
  dialog.title = "新增";
  dialog.height = 160;
  onShow();
};
//编辑
const editBtn = (row: SysNotice) => {
  tags.value = "1";
  //清空表单
  addRef.value?.resetFields();
  dialog.title = "编辑";
  dialog.height = 160;
  onShow();
  nextTick(() => {
    Object.assign(addModel, row);
  });
};
//删除
const deleteBtn = async (row: SysNotice) => {
  let confirm = await global.$myconfirm("确定删除该数据吗?");
  if (confirm) {
    let res = await deleteApi(row);
    if (res && res.code == 200) {
      ElMessage.success(res.msg);
      getList();
    }
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
        getList();
        ElMessage.success(res.msg);
        onClose();
      }
    }
  });
};
//表格数据
const tableList = ref([]);
//表格高度
const tableHeight = ref(0);
//列表查询
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
  getList();
};
//页数改变时触发
const currentChange = (page: number) => {
  searchParm.currentPage = page;
  getList();
};
onMounted(() => {
  getList();
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230;
  });
});
</script>

<style scoped></style>


