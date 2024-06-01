<template>
  <div id="app" class="app scrollEle" :style="appStyle"
       @touchstart="touchStart"
       @touchmove="touchMove"
       @touchend="touchEnd">
    <div class="loadDiv" v-if="this.moveDistance > 0">
      <div v-if="moveState === 0" class="text-center">
        <el-button icon="el-icon-arrow-down" circle></el-button>
      </div>
      <div v-if="moveState === 1" class="text-center">
        <div class="spinner-grow text-success" role="status">
          <span class="sr-only">Loading...</span>
        </div>
      </div>
    </div>
    <el-container :style="mainElContainerStyle">
      <el-header :style="mainElHeaderStyle" :height="mainElHeaderStyle.height">
        <el-button class="mainMenuBtn" :style="mainMenuBtnStyle" type="text"
                   @click="mainMenuBtnClick">
          <i class="el-icon-menu" @click="mainMenuBtnClick" @mouseover="mainMenuBtnClick"></i>
        </el-button>
        <a class="mainLogoBtn" @click="mainLogoBtnClick" :style="mainLogoBtnStyle">
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
                      placeholder="请输入要搜索的内容" @keyup.enter.native="searchInputHandle">
            </el-input>
          </div>
          <div v-show="!showSearchInput" class="toolDiv">
            <el-dropdown>
              <el-button :style="toolBtnStyle" icon="el-icon-s-tools" size="small" circle></el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  首页
                  <a href="/" target="view_window">(公网)</a>
                  <a href="http://web.itkk.org:81/" target="view_window">(公网-备用)</a>
                  <a href="http://192.168.1.100/" target="view_window">(内网)</a>
                </el-dropdown-item>
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
            <el-popover
              placement="top-start"
              trigger="hover">
              <div class="userPanel">
                <el-row>
                  <el-col :span="24">
                    <el-avatar :src="avatarFilePath" fit="fill" shape="square" size="small" icon="el-icon-user-solid"
                               style="vertical-align: middle;">
                    </el-avatar>
                    <span>{{userDto.nickName}}</span>
                    <el-divider></el-divider>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <span><el-button type="text" :style="userDtoBtnStyle"
                                     @click="userCenterBtnClick">个人中心</el-button></span>
                    <el-divider direction="vertical"></el-divider>
                    <span><el-button type="text" :style="logoutBtnStyle"
                                     @click="logoutBtnClick">退出登录</el-button></span>
                  </el-col>
                </el-row>
              </div>
              <el-avatar :src="avatarFilePath" fit="fill" slot="reference" shape="square" size="large"
                         icon="el-icon-user-solid">
              </el-avatar>
            </el-popover>
          </div>
        </div>
      </el-header>
      <el-container :style="mainChildElContainerStyle">
        <el-aside v-show="elAsideShow" width="240px">
          <el-scrollbar style="height:100%;">
            <left-menu :data="menu" @leftMenuSelect="leftMenuSelectHandle"></left-menu>
          </el-scrollbar>
        </el-aside>
        <el-container :style="mainChildElContainerStyle">
          <el-scrollbar class="main-el-scrollbar" style="height:100%;width: 100%;">
            <el-backtop :style="backtopStyle" target=".main-el-scrollbar .el-scrollbar__wrap">
              <i class="el-icon-caret-top"></i>
            </el-backtop>
            <div class="mainDiv">
              <el-main>
                <transition name="el-zoom-in-center">
                  <keep-alive>
                    <router-view/>
                  </keep-alive>
                </transition>
              </el-main>
            </div>
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
      </el-container>
    </el-container>
    <el-drawer
      size="250px"
      :with-header="false"
      :show-close="false"
      :visible.sync="leftMenuDrawerShow"
      direction="ltr">
      <div :style="drawerStyle">
        <el-scrollbar style="height:100%;">
          <div class="drawerHeader">
            <i class="el-icon-s-grid"></i>
            <span slot="title">功能菜单</span>
            <el-divider></el-divider>
          </div>
          <left-menu :data="menu" @leftMenuSelect="leftMenuSelectHandle"></left-menu>
        </el-scrollbar>
      </div>
    </el-drawer>
    <el-drawer
      size="250px"
      :with-header="false"
      :show-close="false"
      :visible.sync="rightUserCenterDrawerShow"
      direction="rtl">
      <div :style="drawerStyle">
        <el-scrollbar style="height:100%;">
          <div class="drawerHeader">
            <i class="el-icon-s-grid"></i>
            <span slot="title">个人中心</span>
            <el-divider></el-divider>
          </div>
          <user-center ref="userCenter"></user-center>
        </el-scrollbar>
      </div>
    </el-drawer>
  </div>
</template>

<script>

  import api_rbac from "./api/rbac";
  import glob from "./assets/js/glob";
  import leftMenu from "./components/leftMenu";
  import userCenter from "./components/userCenter";
  import api_file from "./api/file";

  export default {
    name: 'App',
    components: {
      userCenter,
      leftMenu
    },
    data() {
      return {
        golbSetting: {
          appLogoName: 'ITKK',
          copyRight: '© 2019-2020 itkk.org',
          appBackground: "#FAFAFA",
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
        mainChildElContainerStyle: {
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
        userDtoBtnStyle: {},
        logoutBtnStyle: {},
        showSearchInput: false,
        searchInputText: null,
        userDto: {},
        avatarFilePath: null,
        menu: [],
        elAsideShow: true,
        leftMenuDrawerShow: false,
        drawerStyle: {},
        rightUserCenterDrawerShow: false,
        startY: '',
        moveDistance: 0,
        moveState: 0,
      }
    },
    mounted() {
      {
        var startY = 0;
        document.addEventListener('touchstart', function (e) {
          startY = Number(e.touches[0].pageY);
        });
        let ele = document.querySelector('.scrollEle');
        ele.ontouchmove = function (e) {
          let scrollbarDom = document.getElementsByClassName('el-scrollbar__wrap')[1];
          let point = e.touches[0];
          let eleTop = scrollbarDom.scrollTop;
          let eleTouchBottom = scrollbarDom.scrollHeight - scrollbarDom.offsetHeight;
          if (eleTop === 0) {
            if (point.clientY > startY) {
              e.preventDefault();
            }
          } else if (eleTop === eleTouchBottom) {
            if (point.clientY < startY) {
              e.preventDefault()
            }
          }
        };
      }
      //初始化
      this.init();
    },
    methods: {
      //开始拖拽
      touchStart(e) {
        //下拉刷新逻辑
        {
          this.moveDistance = 0;
          this.startY = 0;
        }
      },
      //拖拽中
      touchMove(e) {
        //下拉刷新逻辑
        {
          let scrollTop = document.getElementsByClassName('el-scrollbar__wrap')[1].scrollTop;
          if (scrollTop > 0) {
            return;
          } else {
            if (this.startY === 0) {
              this.startY = e.targetTouches[0].clientY;
            }
          }
          let y = e.targetTouches[0].clientY > window.innerHeight ? window.innerHeight : e.targetTouches[0].clientY;
          let move = y - this.startY;
          if (move > 0) {
            this.moveDistance = Math.pow(move, 0.8);
            let loadDivDoms = document.getElementsByClassName('loadDiv');
            if (loadDivDoms && loadDivDoms[0]) {
              loadDivDoms[0].style.transform = "translate3d(0," + (this.moveDistance > 70 ? 70 : parseInt(this.moveDistance)) + 'px' + ",0)";
            }
            if (this.moveDistance > 70) {
              if (this.moveState === 1) return;
              this.moveState = 1;
            } else {
              if (this.moveState === 0) return;
              this.moveState = 0;
            }
          } else {
            this.moveDistance = 0;
            this.moveState = 0;
          }
        }
      },
      //结束拖拽
      touchEnd(e) {
        //下拉刷新逻辑
        {
          if (this.moveDistance > 70) {
            this.$EventBus.$emit(glob.eventNames.globRefreshEventName);
            this.moveDistance = 0;
            this.moveState = 0;
          } else {
            this.moveDistance = 0;
          }
        }
      },
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
        //检查登陆
        glob.checkLogin();
        //获得用户信息
        const userDtoResponse = await api_rbac.infoByToken(glob.getToken());
        this.userDto = userDtoResponse.data.result;
        this.avatarFilePath = api_file.preview(this.userDto.avatarFileId, 40, 40);
        //获得菜单
        const menuResponse = await api_rbac.menu();
        this.menu = menuResponse.data.result;
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
        this.userDtoBtnStyle.color = this.golbSetting.themeBackground;
        this.logoutBtnStyle.color = this.golbSetting.themeBackground;
        this.logoutBtnStyle.background = this.golbSetting.appBackground;
      },
      //resize监听器
      resizeListener() {
        //设定容器高度
        this.appStyle.height = window.innerHeight + 'px';
        this.mainElContainerStyle.height = window.innerHeight + 'px';
        this.mainChildElContainerStyle.height = (window.innerHeight - 50) + 'px';
        this.drawerStyle.height = window.innerHeight + 'px';
        //设定框宽度
        this.searchInputStyle.width = window.innerWidth - 200 - 10 + 'px';
        //判断是否显示菜单栏
        this.elAsideShow = window.innerWidth > glob.minWidth ? true : false;
      },
      //log点击事件
      mainLogoBtnClick() {
        window.location.href = '/';
      },
      //搜索输入框显示和隐藏
      searchInputDivShowHidden() {
        this.showSearchInput = !this.showSearchInput;
      },
      //退出登录按钮事件
      logoutBtnClick() {
        const loading = this.$loading({lock: true, text: '操作中'});
        api_rbac.logout().then(response => window.location.href = '/').finally(() => loading.close());
      },
      //个人中心按钮点击事件
      userCenterBtnClick() {
        this.rightUserCenterDrawerShow = true;
        this.$nextTick(() => {
          this.$refs.userCenter.init();
        });
      },
      //菜单按钮点击
      mainMenuBtnClick() {
        this.leftMenuDrawerShow = true;
      },
      //左边的菜单选择
      leftMenuSelectHandle() {
        if (this.leftMenuDrawerShow) {
          this.leftMenuDrawerShow = false;
        }
      },
      //搜索框输入
      searchInputHandle() {
        this.$EventBus.$emit(glob.eventNames.globSearchEventName, this.searchInputText);
      }
    }
  }
</script>
<style lang="scss">
  .loadDiv {
    position: absolute;
    width: 100%;
    z-index: 2000;
  }

  .mainDiv {
    padding: 10px 10px 10px 10px;
  }

  .el-drawer {
    background-color: #F5F5F5;
  }

  .drawerHeader {
    padding: 10px 20px;
    text-align: left;
    font-size: 20px;

    .el-divider--horizontal {
      margin: 10px 0px 0px 0px;
    }
  }

  .top-form {
    .el-form {
      .el-form-item {
        margin-bottom: 0px !important;

        label {
          margin-bottom: 0px !important;
        }

        .el-form-item__label {
          padding: 0 0 0px !important;
        }

        .el-button {
          margin-top: 15px;
        }
      }
    }
  }

  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
</style>
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

      .el-aside {
        margin: 0;
        padding: 0;
        height: 100%;
        border-right: solid 1px #e6e6e6;
        background: #F5F5F5;
        box-shadow: rgba(0, 0, 0, 0.08) 0px 1px 4px 0px;

        .el-menu {
          border-right: solid 0px #e6e6e6 !important;
          height: 100% !important;
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

  .userPanel {
    .el-divider--horizontal {
      margin: 10px 0px;
    }

    .el-button {
      padding-top: 0px;
      padding-bottom: 0px;
    }
  }
</style>
