<template>
  <div class="main top-form">
    <el-form ref="form" :model="form" label-position="top" size="medium">
      <el-card class="contentBar">
        <el-form-item label="* 文章标题">
          <el-input v-model="form.title" placeholder="文章标题,100个字符以内"></el-input>
        </el-form-item>
        <el-form-item label="* 文章内容">
          <mavon-editor v-model="form.content"
                        :subfield="false"
                        :toolbarsFlag="true"
                        :toolbars="toolbars"
                        :autofocus="false"
                        style="min-height: 600px;z-index: 0;"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="success">发布文章</el-button>
          <el-button type="success">保存草稿</el-button>
        </el-form-item>
      </el-card>
    </el-form>
  </div>
</template>

<script>

  import glob from "../../assets/js/glob";

  export default {
    name: "artice-add",
    data() {
      return {
        form: {
          title: null,
          content: ''
        },
        toolbars: {},
        toolbarsFull: {
          bold: true, // 粗体
          italic: true, // 斜体
          header: true, // 标题
          underline: true, // 下划线
          strikethrough: true, // 中划线
          mark: true, // 标记
          superscript: true, // 上角标
          subscript: true, // 下角标
          quote: true, // 引用
          ol: true, // 有序列表
          ul: true, // 无序列表
          link: true, // 链接
          imagelink: true, // 图片链接
          code: true, // code
          table: true, // 表格
          fullscreen: false, // 全屏编辑
          readmodel: false, // 沉浸式阅读
          htmlcode: false, // 展示html源码
          help: true, // 帮助
          /* 1.3.5 */
          undo: true, // 上一步
          redo: true, // 下一步
          trash: true, // 清空
          save: false, // 保存（触发events中的save事件）
          /* 1.4.2 */
          navigation: true, // 导航目录
          /* 2.1.8 */
          alignleft: true, // 左对齐
          aligncenter: true, // 居中
          alignright: true, // 右对齐
          /* 2.2.1 */
          subfield: true, // 单双栏模式
          preview: true, // 预览
        },
        toolbarsMin: {
          imagelink: true, // 图片链接
          help: true, // 帮助
          undo: true, // 上一步
          redo: true, // 下一步
          preview: true, // 预览
        }
      }
    },
    activated() {
      //重置
      this.reset();
    },
    deactivated() {

    },
    methods: {
      //重置
      reset() {
        //重置表单
        this.form = {
          title: null,
          content: ''
        };
        //设置工具栏
        if (window.innerWidth > glob.minWidth) {
          this.toolbars = this.toolbarsFull;
        } else {
          this.toolbars = this.toolbarsMin;
        }
      }
    }
  }
</script>

<style lang="scss" scoped>
  .main {
    padding: 10px 10px 10px 10px;

    .el-card {
      border-radius: 0px;
    }

    .contentBar {

    }
  }
</style>
