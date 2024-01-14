<template>
  <div id="app" class="app" :style="appStyle">
    <el-container :style="mainElContainerStyle">
      <el-header :style="mainElHeaderStyle" :height="mainElHeaderStyle.height">
        <el-button class="mainMenuBtn" :style="mainMenuBtnStyle" type="text">
          <i class="el-icon-menu"></i>
        </el-button>
        <a class="mainLogoBtn" href="./" :style="mainLogoBtnStyle">
          <span>
            <i>{{golbSetting.appLogoName}}</i>
          </span>
        </a>
        <div class="mainMenuRightDiv">
          <div class="searchDiv">
            <el-button :style="searchBtnStyle" icon="el-icon-search" size="small" circle
                       @click="searchInputDivShowHidden"></el-button>
          </div>
          <div v-show="showSearchInput" class="searchInputDiv">
            <el-input ref="searchInput" v-model="searchInputText" :style="searchInputStyle" clearable
                      placeholder="请输入要搜索的内容">
            </el-input>
          </div>
          <div v-show="!showSearchInput" class="toolDiv">
            <el-dropdown>
              <el-button :style="toolBtnStyle" icon="el-icon-s-tools" size="small" circle></el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  构建流水线
                  <a href="/jenkins/" target="view_window">(公网)</a>
                  <a href="http://web.itkk.org:81/jenkins/" target="view_window">(公网-备用)</a>
                  <a href="http://192.168.1.100/jenkins/" target="view_window">(内网)</a>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div v-show="!showSearchInput" class="bellDiv">
            <el-badge is-dot>
              <el-button :style="bellBtnStyle" icon="el-icon-bell" size="small" circle></el-button>
            </el-badge>
          </div>
          <div v-show="!showSearchInput" class="avatarDiv">
            <el-avatar shape="square" size="large" icon="el-icon-user-solid">
            </el-avatar>
          </div>
        </div>
      </el-header>
      <el-scrollbar style="height:100%">
        <el-backtop :style="backtopStyle" target=".el-scrollbar__wrap">
          <i class="el-icon-caret-top"></i>
        </el-backtop>
        <el-main>
          <transition name="el-zoom-in-center">
            <keep-alive>
              <router-view/>
            </keep-alive>
          </transition>
        </el-main>
        <el-footer :style="mainFoterStyle">
          <div class="footer">
            <el-divider></el-divider>
            <div class="copyRight text-center">
              <span>{{golbSetting.copyRight}}</span>
            </div>
          </div>
        </el-footer>
      </el-scrollbar>
    </el-container>
  </div>
</template>

<script>

  import glob from "./assets/js/glob";

  export default {
    name: 'App',
    data() {
      return {
        golbSetting: {
          appLogoName: 'ITKK',
          copyRight: '© 2019-2020 itkk.org',
          appBackground: "#F2F2F2",
          headerBackground: "#FFFFFF",
          menuBtnColor: "#FFFFFF",
          themeBackground: "#FF6A00",
          themeColor: "#FFFFFF",
        },
        appStyle: {},
        mainMenuBtnStyle: {},
        mainLogoBtnStyle: {},
        searchBtnStyle: {
          outline: "none"
        },
        toolBtnStyle: {
          outline: "none"
        },
        loginBtnStyle: {
          outline: "none"
        },
        registeredBtnStyle: {
          outline: "none"
        },
        bellBtnStyle: {
          outline: "none"
        },
        backtopStyle: {},
        mainElContainerStyle: {
          height: 0
        },
        mainElHeaderStyle: {
          height: "50px",
        },
        mainFoterStyle: {},
        searchInputStyle: {
          "max-width": "400px",
          width: "150px"
        },
        showSearchInput: false,
        searchInputText: null
      }
    },
    created() {
      //初始化
      this.init();
    },
    mounted() {
      //检查登陆
      glob.checkLogin();
    },
    methods: {
      //初始化
      init() {
        //初始化(异步)
        this.initAsync();
      },
      //初始化(异步)
      async initAsync() {
        //重设主题属性
        this.resetTheme();
        //注册浏览器resize监听
        window.addEventListener('resize', this.resizeListener);
        //主动调用一次resize监听
        this.resizeListener();
      },
      //重设主题属性
      resetTheme() {
        this.appStyle.background = this.golbSetting.appBackground;
        this.mainElHeaderStyle.background = this.golbSetting.headerBackground;
        this.mainMenuBtnStyle.background = "none " + this.golbSetting.themeBackground;
        this.loginBtnStyle.background = this.golbSetting.themeBackground;
        this.registeredBtnStyle.background = this.golbSetting.themeBackground;
        this.mainMenuBtnStyle.color = this.golbSetting.menuBtnColor;
        this.mainLogoBtnStyle.color = this.golbSetting.themeBackground;
        this.backtopStyle.background = this.golbSetting.themeBackground;
        this.backtopStyle.color = this.golbSetting.themeColor;
      },
      //resize监听器
      resizeListener() {
        //设定容器高度
        this.mainElContainerStyle.height = window.innerHeight + 'px';
        //设定框宽度
        this.searchInputStyle.width = window.innerWidth - 200 - 10 + 'px';
      },
      //搜索输入框显示和隐藏
      searchInputDivShowHidden() {
        this.showSearchInput = !this.showSearchInput;
      }
    }
  }
</script>

<style lang="scss" scoped>
  .app {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
    font-family: 'Microsoft yahei';
    font-size: 13px;
    font-weight: 400;

    .el-container {

      .el-header {
        box-shadow: rgba(0, 0, 0, 0.08) 0px 1px 4px 0px;
        height: 50px;
        margin: 0;
        padding: 0;

        .mainMenuBtn {
          width: 50px;
          height: 50px;
          border-radius: 0px !important;
          box-sizing: border-box;
          line-height: inherit;
          cursor: pointer;
          font-family: inherit;
          font-size: inherit;
          display: inline-block;
          vertical-align: middle;
          text-align: center;
          white-space: nowrap;
          padding: 0px;
          outline: medium;
          text-decoration: none;
          transition: all 0.3s ease-out;
          border-width: initial;
          border-style: none;
          border-color: initial;
          border-image: initial;
          overflow: hidden;
          font-size: 30px;
        }

        .mainLogoBtn {
          height: 50px;
          padding: 0px 12px;
          cursor: pointer;

          span {
            font-size: 30px;
            font-weight: bold;
            font-family: inherit;
            display: inline-block;
            vertical-align: middle;
            text-align: center;
          }
        }

        .mainMenuRightDiv {
          float: right;
          height: 50px;

          .searchDiv {
            height: 50px;
            padding-right: 8px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            font-size: 18px;
            cursor: pointer;
          }

          .searchInputDiv {
            height: 50px;
            padding-right: 8px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            font-size: 18px;
            cursor: pointer;
          }

          .toolDiv {
            height: 50px;
            padding-right: 8px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            font-size: 18px;
            cursor: pointer;
          }

          .bellDiv {
            height: 50px;
            padding-right: 8px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            font-size: 18px;
            cursor: pointer;
          }

          .avatarDiv {
            height: 50px;
            padding-right: 10px;
            padding-top: 4px;
            display: table-cell;
            vertical-align: middle;
            text-align: center;
            cursor: pointer;
          }
        }
      }

      .el-main {
        margin: 0;
        padding: 0;
        overflow: hidden;
      }

      .el-footer {
        margin: 0;
        padding: 0;
        overflow: hidden;

        .footer {
          margin-top: 10px;

          .el-divider--horizontal {
            margin: 0px 0px 10px 0px;
            box-shadow: rgba(0, 0, 0, 0.08) 0px 1px 4px 0px;
          }

          .copyRight {
            max-width: 1200px;
            margin: auto !important;
          }
        }
      }
    }
  }
</style>
