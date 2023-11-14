// element引入列表
//完整组件参照 : https://github.com/ElemeFE/element/blob/master/components.json
import Vue from 'vue';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/scss/element-variables.scss';
import {
  Aside,
  Backtop,
  Badge,
  Button,
  ButtonGroup,
  Card,
  Container,
  Divider,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  Footer,
  Header,
  Input,
  Loading,
  Main,
  Message,
  MessageBox,
  Notification,
  Scrollbar,
  Transfer
} from 'element-ui';

Vue.use(Backtop);
Vue.use(Dropdown);
Vue.use(DropdownItem);
Vue.use(Button);
Vue.use(ButtonGroup);
Vue.use(Badge);
Vue.use(Card);
Vue.use(Transfer);
Vue.use(Container);
Vue.use(Header);
Vue.use(Aside);
Vue.use(Main);
Vue.use(Footer);
Vue.use(Scrollbar);
Vue.use(Divider);
Vue.use(Loading.directive);
Vue.use(DropdownMenu);
Vue.use(Input);
// Vue.use(Pagination);
// Vue.use(Dialog);
// Vue.use(Autocomplete);
// Vue.use(Menu);
// Vue.use(Submenu);
// Vue.use(MenuItem);
// Vue.use(MenuItemGroup);
// Vue.use(InputNumber);
// Vue.use(Radio);
// Vue.use(RadioGroup);
// Vue.use(RadioButton);
// Vue.use(Checkbox);
// Vue.use(CheckboxButton);
// Vue.use(CheckboxGroup);
// Vue.use(Switch);
// Vue.use(Select);
// Vue.use(Option);
// Vue.use(OptionGroup);
// Vue.use(Table);
// Vue.use(TableColumn);
// Vue.use(DatePicker);
// Vue.use(TimeSelect);
// Vue.use(TimePicker);
// Vue.use(Popover);
// Vue.use(Tooltip);
// Vue.use(Breadcrumb);
// Vue.use(BreadcrumbItem);
// Vue.use(Form);
// Vue.use(FormItem);
// Vue.use(Tabs);
// Vue.use(TabPane);
// Vue.use(Tag);
// Vue.use(Tree);
// Vue.use(Alert);
// Vue.use(Slider);
// Vue.use(Icon);
// Vue.use(Row);
// Vue.use(Col);
// Vue.use(Upload);
// Vue.use(Progress);
// Vue.use(Rate);
// Vue.use(Steps);
// Vue.use(Step);
// Vue.use(Carousel);
// Vue.use(CarouselItem);
// Vue.use(Collapse);
// Vue.use(CollapseItem);
// Vue.use(Cascader);
// Vue.use(ColorPicker);

Vue.prototype.$loading = Loading.service;
Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$notify = Notification;
Vue.prototype.$message = Message;
