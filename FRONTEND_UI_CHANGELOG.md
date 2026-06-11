# 记账本系统 — 前端界面修改说明文档

## 一、项目概述

| 项目 | 说明 |
|------|------|
| 项目名称 | 记账本（JiZhangBen） |
| 技术栈 | Android (Java) + Material Design + Spring Boot (后端) |
| 最低API | API 21+ |

---

## 二、本次修改总览

### 2.1 后端改动（Spring Boot）

#### 文件清单

| 文件路径 | 改动类型 | 改动内容 |
|----------|----------|----------|
| `src/main/java/.../entity/User.java` | **重写** | 用户实体新增 phone/name/occupation/gender 字段，登录账号从 username 改为 phone |
| `src/main/java/.../model/RegisterRequest.java` | **重写** | 新增 phone/confirmPassword/name/occupation/gender 字段 |
| `src/main/java/.../model/LoginRequest.java` | **重写** | 字段改为 phone（手机号）+ password |
| `src/main/java/.../service/UserService.java` | **重写** | 注册逻辑：手机号格式校验、确认密码校验；登录逻辑改为按手机号查询 |
| `src/main/java/.../mapper/UserMapper.java` | **修改** | 新增 findByPhone 方法，insert 改为插入完整字段 |

### 2.2 数据库变更

#### user 表结构（最新版）

```sql
-- 如果已存在旧表，执行以下 ALTER 操作：
ALTER TABLE user ADD COLUMN phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号' AFTER id;
ALTER TABLE user ADD COLUMN name VARCHAR(50) DEFAULT NULL COMMENT '姓名' AFTER password;
ALTER TABLE user ADD COLUMN occupation VARCHAR(50) DEFAULT NULL COMMENT '职业' AFTER age;
ALTER TABLE user ADD COLUMN gender VARCHAR(10) DEFAULT NULL COMMENT '性别(男/女)' AFTER occupation;

-- 如果是全新建表：
CREATE DATABASE IF NOT EXISTS bookkeep_db;
USE bookkeep_db;

CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    name VARCHAR(50) DEFAULT NULL COMMENT '姓名',
    age INT DEFAULT NULL COMMENT '年龄',
    occupation VARCHAR(50) DEFAULT NULL COMMENT '职业',
    gender VARCHAR(10) DEFAULT NULL COMMENT '性别：男/女',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';
```

---

## 三、Android 前端界面改动

### 3.1 全局资源

#### 颜色主题 (`res/values/colors.xml`)

| 颜色名 | 色值 | 用途 |
|--------|------|------|
| `primary` | #FF2196F3 | 主色调（蓝色） |
| `primary_dark` | #FF1976D2 | 深蓝色（状态栏） |
| `primary_light` | #FFBBDEFB | 浅蓝色 |
| `income_green` | #FF4CAF50 | 收入颜色（绿色） |
| `expense_red` | #FFF44336 | 支出颜色（红色） |
| `bg_main` | #FFF5F5F5 | 页面背景色（浅灰） |
| `bg_card` | #FFFFFFFF | 卡片背景（白色） |
| `text_primary` | #FF212121 | 主文字颜色 |
| `text_secondary` | #FF757575 | 次要文字颜色 |
| `text_hint` | #FFBDBDBD | 提示文字颜色 |
| `divider` | #FFE0E0E0 | 分割线颜色 |

#### 主题 (`res/values/themes.xml`)

- 父主题从 `DarkActionBar` 改为 **NoActionBar**（自定义标题栏）
- 主色调统一为蓝色系
- 状态栏和导航栏均为深蓝色

#### 字符串 (`res/values/strings.xml`)

- 应用名称改为"记账本"
- 底部导航：首页 / 统计 / 我的
- 新增首页、统计页、个人中心相关字符串

#### 数组资源 (`res/values/arrays.xml`) [新建]

- 性别选项数组：请选择性别 / 男 / 女

#### Drawable资源 (`res/drawable/edit_text_bg.xml`) [新建]

- 输入框圆角背景：白色底 + 圆角10dp + 浅灰边框

---

### 3.2 登录页面

#### 布局文件: `activity_login.xml`

| 控件 | ID | 类型 | 说明 |
|------|----|------|------|
| 手机号输入框 | `et_username` | EditText | inputType=phone, maxLength=11 |
| 密码输入框 | `et_password` | EditText | inputType=textPassword |
| 登录按钮 | `btn_login` | Button | 蓝色填充按钮 |
| 注册按钮 | `btn_register` | Button | 蓝色描边按钮 |

**改动要点：**
- 输入框提示文字从"用户名"改为"请输入手机号"
- 输入框类型设为 phone（数字键盘）
- 手机号限制最大11位
- 按钮样式统一为Material Design风格
- 整体背景使用浅灰色 `@color/bg_main`
- 输入框使用自定义圆角背景 `@drawable/edit_text_bg`

#### 逻辑文件: `LoginActivity.java`

- 校验提示改为"请输入手机号"
- 登录参数传递 phone 替代 username
- 注册跳转保持不变

---

### 3.3 注册页面

#### 布局文件: `activity_register.xml`

| 控件 | ID | 类型 | 说明 |
|------|----|------|------|
| 标题 | - | TextView | "注册新账号"，26sp，加粗 |
| 手机号输入框 | `et_reg_phone` | EditText | 必填，phone类型，maxLength=11 |
| 密码输入框 | `et_reg_password` | EditText | 必填，密码类型 |
| 确认密码输入框 | `et_reg_confirm_password` | EditText | 必填，密码类型 |
| 分割提示 | - | TextView | "个人信息（选填）" |
| 姓名输入框 | `et_reg_name` | EditText | 选填 |
| 年龄输入框 | `et_reg_age` | EditText | 选填，数字类型 |
| 性别选择器 | `spinner_gender` | Spinner | 下拉选择：请选择性别/男/女 |
| 职业输入框 | `et_reg_occupation` | EditText | 选填 |
| 注册按钮 | `btn_register_confirm` | Button | "立即注册"，全宽，圆角12dp |

**布局特点：**
- 外层包裹 ScrollView，支持小屏幕滚动
- 表单分为两段：**必填区**（手机号/密码/确认密码）和 **选填区**（个人信息）
- 年龄和性别横排排列，节省空间
- 所有输入框统一使用圆角背景样式

#### 逻辑文件: `RegisterActivity.java`

**校验规则：**
1. 手机号非空 → 提示"请输入手机号"
2. 手机号格式 → 正则 `^1[3-9]\d{9}$` 校验11位手机号
3. 密码非空 → 提示"请输入密码"
4. 两次密码一致 → 提示"两次输入的密码不一致"

**注册调用：**
```
userRepository.register(phone, password, confirmPassword, name, age, occupation, gender, callback)
```

---

### 3.4 数据模型

#### LoginRequest.java (Android)

```java
public class LoginRequest {
    private String phone;    // 手机号（替代原username）
    private String password;

    // 默认构造函数 + 带参构造函数 + Getter/Setter
}
```

#### RegisterRequest.java (Android)

```java
public class RegisterRequest {
    private String phone;           // 手机号
    private String password;        // 密码
    private String confirmPassword;  // 确认密码
    private String name;            // 姓名
    private String age;             // 年龄
    private String occupation;      // 职业
    private String gender;          // 性别

    // 默认构造函数 + 完整带参构造函数 + 全部Getter/Setter
}
```

#### UserRepository.java

- `register()` 方法签名更新为7个参数（phone/password/confirmPassword/name/age/occupation/gender）
- `login()` 方法签名更新为2个参数（phone/password）
- 内部构建对应的 Request 对象发送给后端

---

## 四、主界面UI设计

### 4.1 底部导航

三个Tab页面：

| Tab | 图标 | 标题 | 对应Fragment |
|-----|------|------|-------------|
| 首页 | ic_home_black_24dp | 首页 | HomeFragment |
| 统计 | ic_dashboard_black_24dp | 统计 | DashboardFragment |
| 我的 | ic_notifications_black_24dp | 我的 | NotificationsFragment |

### 4.2 首页 (HomeFragment)

**布局结构：**

```
┌─────────────────────────────┐
│ ┌─────────────────────────┐ │
│ │ 2024年6月          [蓝] │ │  ← 蓝色卡片(MaterialCardView)
│ │ ¥ 0.00                 │ │     显示当前月份结余
│ │ 本月收入      本月支出   │ │
│ │ +¥0.00       -¥0.00    │ │
│ └─────────────────────────┘ │
│ 最近记录                     │
│ ┌─────────────────────────┐ │
│ │ 🍜 餐饮    -¥25.00   > │ │  ← 记账记录列表项(item_record)
│ │    午餐    06-10 12:30  │ │     卡片式设计，含图标/分类/备注/金额/日期
│ └─────────────────────────┘ │
│                      [ + ]  │  ← FAB添加按钮(右下角)
└─────────────────────────────┘
```

**控件ID对照表：**

| ID | 类型 | 用途 |
|----|------|------|
| `card_summary` | MaterialCardView | 月度汇总卡片容器 |
| `tv_month_label` | TextView | 当前年月标签 |
| `tv_balance` | TextView | 结余金额 |
| `tv_income` | TextView | 本月收入 |
| `tv_expense` | TextView | 本月支出 |
| `tv_record_title` | TextView | "最近记录"标题 |
| `rv_records` | RecyclerView | 记账列表 |
| `layout_empty` | LinearLayout | 空状态提示（无数据时显示） |
| `fab_add` | FloatingActionButton | 添加记账按钮 |

**新建布局文件：**

| 文件 | 说明 |
|------|------|
| `item_record.xml` | 记账记录列表项：分类图标 + 分类名/备注 + 金额(红/绿) + 日期时间 |

### 4.3 统计页 (DashboardFragment)

**布局结构：**

```
┌─────────────────────────────┐
│ 收支统计                    │  ← 标题 22sp 加粗
│                             │
│ ┌─────────────────────────┐ │
│ │ 总收入       +¥0.00    │ │  ← 白色卡片
│ │ ─────────────────────── │ │     收入(绿)/支出(红)/结余(蓝)
│ │ 总支出       -¥0.00    │ │
│ │ ─────────────────────── │ │
│ │ 结余         ¥0.00    │ │
│ └─────────────────────────┘ │
│                             │
│ 分类统计                    │
│ ┌─────────────────────────┐ │
│ │ ● 餐饮    35%  ¥350.00 │ │  ← 分类列表
│ │ ● 交通    20%  ¥200.00 │ │     颜色标识 + 名称 + 占比 + 金额
│ │ ● 购物    45%  ¥450.00 │ │
│ └─────────────────────────┘ │
└─────────────────────────────┘
```

**控件ID对照表：**

| ID | 类型 | 用途 |
|----|------|------|
| `card_stat_summary` | MaterialCardView | 收支汇总卡片 |
| `tv_stat_income` | TextView | 总收入（绿色） |
| `tv_stat_expense` | TextView | 总支出（红色） |
| `tv_stat_balance` | TextView | 结余（蓝色） |
| `layout_category_list` | LinearLayout | 分类统计列表容器 |

**新建布局文件：**

| 文件 | 说明 |
|------|------|
| `item_category.xml` | 分类统计项：颜色圆点 + 分类名 + 百分比 + 金额 |

### 4.4 个人中心页 (NotificationsFragment)

**布局结构：**

```
┌─────────────────────────────┐
│                             │
│ ┌─────────────────────────┐ │
│ │ 👤 未登录               │ │  ← 蓝色用户卡片
│ │ 点击登录享受更多功能     │ │     头像 + 用户名 + 副标题 + 箭头
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ 📊 数据管理          >  │ │  ← 功能菜单卡片
│ │ ⚙️ 设置             >  │ │     图标 + 文字 + 右箭头
│ │ ℹ️ 关于应用         >  │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │       退出登录           │ │  ← 红色描边按钮
│ └─────────────────────────┘ │
└─────────────────────────────┘
```

**控件ID与功能对照表：**

| ID | 类型 | 点击行为 |
|----|------|----------|
| `iv_avatar` | ImageView | （预留：点击更换头像） |
| `tv_profile_username` | TextView | 显示当前登录用户名 |
| `menu_data_manage` | LinearLayout | Snackbar提示"数据管理功能开发中..." |
| `menu_settings` | LinearLayout | Snackbar提示"设置功能开发中..." |
| `menu_about` | LinearLayout | Snackbar显示"记账本 v1.0.0" |
| `btn_logout` | MaterialButton | 跳转回 LoginActivity 并清除任务栈 |

---

## 五、ViewModel数据模型

| Fragment | ViewModel | LiveData字段 |
|----------|-----------|-------------|
| HomeFragment | HomeViewModel | balance(Double), income(Double), expense(Double) |
| DashboardFragment | DashboardViewModel | totalIncome(Double), totalExpense(Double), balance(Double) |
| NotificationsFragment | NotificationsViewModel | username(String) |

---

## 六、文件修改汇总

### 新建文件（8个）

| 文件路径 | 说明 |
|----------|------|
| `work_02/app/src/main/res/layout/item_record.xml` | 记账记录列表项布局 |
| `work_02/app/src/main/res/layout/item_category.xml` | 分类统计项布局 |
| `work_02/app/src/main/res/drawable/edit_text_bg.xml` | 输入框圆角背景 |
| `work_02/app/src/main/res/values/arrays.xml` | 性别选项数组资源 |
| `work_02/app/src/main/res/values-night/themes.xml` | 夜间模式主题（已有，未改） |

### 修改文件（17个）

| 文件路径 | 改动概述 |
|----------|----------|
| `work_02/app/src/main/res/values/colors.xml` | 重写为蓝色系配色方案 |
| `work_02/app/src/main/res/values/themes.xml` | NoActionBar蓝色主题 |
| `work_02/app/src/main/res/values/strings.xml` | 中文化字符串资源 |
| `work_02/app/src/main/res/menu/bottom_nav_menu.xml` | 导航菜单中文标题 |
| `work_02/app/src/main/res/layout/activity_login.xml` | 手机号登录页面重设计 |
| `work_02/app/src/main/res/layout/activity_register.xml` | 注册页面添加姓名/职业/性别字段 |
| `work_02/app/src/main/res/layout/activity_main.xml` | 主框架（未改） |
| `work_02/app/src/main/res/layout/fragment_home.xml` | 首页：月度卡片+列表+FAB |
| `work_02/app/src/main/res/layout/fragment_dashboard.xml` | 统计页：收支汇总+分类列表 |
| `work_02/app/src/main/res/layout/fragment_notifications.xml` | 我的：用户卡片+菜单+退出 |
| `work_02/app/src/main/java/.../LoginActivity.java` | 改为手机号登录逻辑 |
| `work_02/app/src/main/java/.../RegisterActivity.java` | 完整注册逻辑+新字段 |
| `work_02/app/src/main/java/.../model/LoginRequest.java` | 改为phone字段 |
| `work_02/app/src/main/java/.../model/RegisterRequest.java` | 新增所有注册字段 |
| `work_02/app/src/main/java/.../repository/UserRepository.java` | register/login方法适配新参数 |
| `work_02/app/src/main/java/.../ui/home/HomeFragment.java` | 首页绑定新布局和数据 |
| `work_02/app/src/main/java/.../ui/home/HomeViewModel.java` | 收支LiveData模型 |
| `work_02/app/src/main/java/.../ui/dashboard/DashboardFragment.java` | 统计页绑定新布局和数据 |
| `work_02/app/src/main/java/.../ui/dashboard/DashboardViewModel.java` | 统计LiveData模型 |
| `work_02/app/src/main/java/.../ui/notifications/NotificationsFragment.java` | 我的页面：退出登录+菜单事件 |
| `work_02/app/src/main/java/.../ui/notifications/NotificationsViewModel.java | 用户名LiveData模型 |
| `src/main/java/.../entity/User.java` | 后端实体新增字段 |
| `src/main/java/.../model/RegisterRequest.java` | 后端请求模型更新 |
| `src/main/java/.../model/LoginRequest.java` | 后端请求模型更新 |
| `src/main/java/.../service/UserService.java` | 注册/登录业务逻辑重写 |
| `src/main/java/.../mapper/UserMapper.java` | 新增findByPhone方法 |

---

## 七、后续待开发功能

按照需求文档，以下功能尚未实现，按优先级排序：

| 优先级 | 功能 | 说明 |
|--------|------|------|
| P0 | 记账页面(AddRecordActivity) | 收支选择、分类、金额、日期、备注、图片上传 |
| P0 | record数据库表及CRUD接口 | 后端记账记录的增删改查 |
| P0 | 首页列表真实数据接入 | RecyclerView Adapter + 编辑/删除操作 |
| P1 | 首页月份筛选切换 | 月份下拉选择器 |
| P1 | 我的-记账总次数显示 | 调用后端统计接口 |
| P1 | 我的-个人信息编辑页面 | EditProfileActivity |
| P2 | 支付宝账单图片AI识别 | 上传图片→AI识别→自动填充 |
