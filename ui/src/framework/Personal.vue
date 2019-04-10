<template>

    <div class="personal">
        <div style="height: 50px; background-color: #FFF; padding: 0 30px">
            <img src="../assets/logo.png" alt="" height="24">
            <h2 class="logo">EP 个人账号中心</h2>
        </div>
        <transition name="personal">
            <div class="personal-pad">
                <div class="personal-left">
                    <transition name="personal">
                        <div v-if="showBlock.personalName" class="contents-card card-personal-name">
                            <div class="name-icon">
                                <h2>M</h2>
                            </div>
                            <h2>merjiezo</h2>
                        </div>
                    </transition>
                    <transition name="personal">
                        <div v-if="showBlock.funcbtnGroup" class="contents-card card-funcbtn-group">
                            <ul class="personal-list">
                                <li v-for="(router, index) in routers" :key="index">
                                    <router-link :to="router.path" >{{ router.name }}</router-link>
                                </li>
                            </ul>
                        </div>
                    </transition>
                </div>
                <div class="personal-right">
                    <transition name="personal" mode="out-in">
                        <router-view v-if="showBlock.PersonalForm"></router-view>
                    </transition>
                </div>

            </div>
        </transition>
    </div>


</template>

<script>
    export default {
        data() {
            return {
                routers: [
                    { name: '回到应用', path: '/' },
                    { name: '个人中心', path: '/personal/info' },
                    { name: '反馈', path: '/personal/feedback' },
                    { name: '关于我们', path: '/personal/about' }
                    
                ],
                firstLoad: true,
                showBlock: {
                    personalName: false,
                    funcbtnGroup: false,
                    PersonalForm: false,
                },
                nameWord: '',
                userId: ''
            }
        },

        watch: {
            userId (val) {
                this.nameWord = val.substring(0, 1).toUpperCase();
            },

            firstLoad(val) {
                if (!val) {
                    this.showBlock.personalName = true;
                    this.showBlock.funcbtnGroup = true;
                    this.showBlock.PersonalForm = true;
                }
            }
        },

        created() {
            let vm = this;
            setTimeout(function() {
                vm.firstLoad = false;
            }, 500);

        },
        methods: {
        }
    }

</script>

<style>
    .personal {
        height: 100vh
    }
    .personal img {
        padding-right: 10px;
        vertical-align: sub
    }
    .logo {
        display: inline-block;
        margin: 0px;
        padding: 5px 0;
        line-height: 40px
    }
    
    .personal-pad {
        margin: 20px auto 0 auto;
        width: 980px;
        min-height: 500px
    }
    
    .personal-left {
        float: left;
        width: 290px
    }
    
    .personal-right {
        margin-left: 310px;
        width: 670px
    }
    
    .card-personal-name {
        height: 100px
    }
    
    .card-funcbtn-group {
        padding: 0px;
        height: 350px
    }
    
    .card-personal-form {
        position: relative;
        min-height: 530px
    }

    .personal-form-group {
        padding: 50px
    }

    .personal-form-group:before, .personal-form-group:after {
        content: "";
        clear: both
    }
    
    .card-personal-name>div, .card-personal-name>h2 {
        display: inline-block
    }

    .card-personal-name>h2 {
        font-size: 28px;
        margin-top: 0px;
        padding-left: 20px
    }
    
    .name-icon {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: rgb(74, 118, 218)
    }
    
    .name-icon > h2 {
        box-sizing: border-box;
        font-size: 22px;
        margin: 11px 0;
        text-align: center;
        color: #FFF
    }

    .personal-list {
        list-style: none;
        padding-left: 0px;
        margin-top: 0px;
        margin-bottom: 0px
    }

    .personal-list > li {
        line-height: 25px;
        font-size: 1.1em
    }

    .personal-list > li > a {
        display: inline-block;
        padding: 10px 20px;
        width: 100%;
        height: 25px;
        line-height: 25px;
        outline: none;
        text-decoration: none;
        color: #AAA;
        transition: all .3s;
        -webkit-transition: all .3s
    }

    .personal-list .router-link-active {
        color: #111 !important;
        font-weight: bold
    }

    
    .personal-enter-active {
        animation: personal-in .4s cubic-bezier(.23,1,.32,1)
    }
    
    .personal-leave-active {
        animation: personal-out .4s cubic-bezier(.23,1,.32,1)
    }
    
    @keyframes personal-in {
        0% {
            opacity: 0;
            transform: translateY(120px) scale(.9)
        }
        100% {
            opacity: 1;
            transform: translateY(0px)
        }
    }
    
    @keyframes personal-out {
        0% {
            opacity: 1;
            transform: translateY(0px)
        }
        100% {
            opacity: 0;
            transform: translateY(120px) scale(.9)
        }
    }
</style>