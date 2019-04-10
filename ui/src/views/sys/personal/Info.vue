<template>
    <div class="contents-card card-personal-form">
        <div>个人信息</div>
        <div class="personal-form-group">
            <el-form ref="addForm" :model="addForm" label-width="100px" :rules="form_rules">
                <el-col :span="24">
                    <el-form-item label="账号" prop="userId">
                        <el-input :maxlength="30" v-model="addForm.userId" disabled></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="用户名中文" prop="userName">
                        <el-input :maxlength="50" v-model="addForm.userName"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="修改密码" prop="password">
                        <el-input :maxlength="30" type="password" v-model="addForm.password"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="当前权限" prop="permission">
                        <el-select v-model="addForm.permission" disabled></el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="状态" prop="status">
                        <el-select v-model="addForm.status" disabled></el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="IP登陆限制" prop="ipAddress">
                        <el-input :maxlength="20" v-model="addForm.ipAddress"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item>
                        <el-button type="warning" @click="reset">重置</el-button>
                        <el-button type="primary" @click="doAdd('addForm')">保存</el-button>
                    </el-form-item>
                </el-col>
                
            </el-form>
        </div>
        
    </div>
</template>

<script>
    import utils from '../../../common/util';
    import moment from 'moment';
    export default {

        data() {
            var numberAndAlphabetValidator = (rule, value, callback) => {
                if (utils.isEmpty(value)) {
                    callback(new Error('不能为空！'));
                } else {

                    const patt = new RegExp("^[0-9a-zA-Z]*$");
                    if (!patt.test(value)) {
                        callback(new Error('请输入数字或字母的组合！'));
                    } else {
                        callback();
                    }
                }
            };
            var validateIp = (rule, value, callback) => {
                if (utils.isEmpty(value)) {
                    callback();
                } else {
                    let ref = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
                    if (!ref.test(value)) {
                        callback(new Error('请输入正确的IP地址'));
                    } else {
                        callback();
                    }
                }
            };
            return {
                resetForm: {},
                addForm: {},
                form_rules: {
                    userId: [{
                        required: true,
                        validator: numberAndAlphabetValidator,
                        trigger: 'blur, change'
                    }],
                    status: [{
                        required: true,
                        message: '状态不能为空',
                        trigger: 'blur'
                    }],
                    newpassword: [{
                        required: true,
                        validator: numberAndAlphabetValidator,
                        trigger: 'blur, change'
                    }, ],
                    ipAddress: [{
                        validator: validateIp,
                        trigger: 'change'
                    }]
                },
                ep_config: {
                    id: 'userId',
                    id_type: 'string',
                    restful: {
                        save: '/localuser/addOrUpdateData'
                    }
                }
            }
        },

        created() {
            let vm = this;
            let body = {};
            let url = global.HOST + '/localuser/getUserInfo';


            utils.fetchUtil(url, JSON.stringify(body), vm, function(responseData) {
                console.log(responseData);
                if (responseData.userInfo.createTime !== null) {
                    responseData.userInfo.createTime = moment(responseData.userInfo.createTime).format('YYYY-MM-DD HH:mm:ss')
                }
                if (responseData.userInfo.lastLoginTime !== null) {
                    responseData.userInfo.lastLoginTime = moment(responseData.userInfo.lastLoginTime).format('YYYY-MM-DD HH:mm:ss')
                }
                vm.addForm = responseData.userInfo;
                vm.resetForm.userName = responseData.userInfo.userName;
                vm.resetForm.ipAddress = responseData.userInfo.ipAddress;
                vm.addForm.password = '';
                vm.$parent.userId = vm.addForm.userId;
            }, function(e) {
                vm.$message.error({
                    showClose: true,
                    message: '数据更新失败,请稍后再试！'
                });
            });
        },
        methods: {
            doAdd(addForm) {
                const vm = this;
                vm.$refs[addForm].validate((valid) => {
                    if (valid) {

                        let body = {};
                        vm.addForm.action = 1;
                        body.addForm = JSON.stringify(vm.addForm);

                        utils.fetchUtil(global.HOST + vm.ep_config.restful.save, JSON.stringify(body), vm, function(data) {
                            vm.$message({
                                showClose: true,
                                message: '更新成功！'
                            });
                        }, function(e) {
                            vm.$message.error({
                                showClose: true,
                                message: '添加失败,请稍后再试！'
                            });
                        });
                    } else {
                        return false;
                    }
                });
            },

            reset() {
                this.addForm.userName = this.resetForm.userName;
                this.addForm.ipAddress = this.resetForm.ipAddress;
                this.addForm.password = '';
            }


        }
    }
</script>

<style>

</style>