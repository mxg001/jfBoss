function initSystem(stateProvider){
	    /*欢迎页面*/
		stateProvider.state('welcome', {
	        abstract: true,
	        url: "/welcome",
	        templateUrl: "views/common/content.html"
	    })
	    .state('welcome.index', {
	        url: "/index",
	        data: {pageTitle: '欢迎登陆'},
	        templateUrl: "views/welcome.html"
	    })


        /****************************************用户中心******************************************/
        .state('user', {
            abstract: true,
            url: "/user",
            templateUrl: "views/common/content.html"
        })
        .state('user.user', {
            url: "/user/user",
            views: {
                "": {templateUrl: "views/user/user.html"},
                "modalRole@user.user": {templateUrl: "views/user/modalRole.html"},
                "modalRight@user.user": {templateUrl: "views/user/modalRight.html"}
            },
            data: {pageTitle: '用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/userCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.userAddMenu', {
            url: "/user/userAddMenu:id/:userName",
            templateUrl: "views/user/userAddMenu.html",
            data: {pageTitle: '权限添加菜单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/userAddMenuCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.role', {
            url: "/user/role",
            views: {
                "": {templateUrl: "views/user/role.html"},
                "modalUser@user.role": {templateUrl: "views/user/modalUser.html"},
                "modalRight@user.role": {templateUrl: "views/user/modalRight.html"}
            },
            data: {pageTitle: "角色管理"},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ['js/controllers/user/roleCtrl2.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.right', {
            url: "/user/right",
            views: {
                "": {templateUrl: "views/user/right.html"},
                "modalUser@user.right": {templateUrl: "views/user/modalUser.html"},
                "modalRole@user.right": {templateUrl: "views/user/modalRole.html"},
                "modalMenu@user.right": {templateUrl: "views/user/modalMenu.html"}
            },
            data: {pageTitle: "权限管理"},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ["js/controllers/user/rightCtrl.js?ver=" + verNo]
                    });
                }]
            }
        })
        .state('user.rightAddMenu', {
            url: "/user/rightAddMenu:id/:rightName",
            templateUrl: "views/user/role2.html",
            data: {pageTitle: '权限添加菜单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/roleCtrl2.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.menu', {
            url: "/user/menu",
            views: {
                "": {templateUrl: "views/user/menu2.html"},
                "modalRight@user.menu": {templateUrl: "views/user/modalRight.html"}
            },
            data: {pageTitle: '菜单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngJsTree');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/menuCtrl2.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.menuAddRight', {
            url: "/user/menuAddRight:id",
            templateUrl: "views/user/menuAddRight.html",
            data: {pageTitle: '菜单添加权限'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/menuAddRightCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('user.dictionary', {
            url: "/user/dictionary",
            templateUrl: "views/user/dictionary.html",
            data: {pageTitle: "数据字典"},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ["$ocLazyLoad", function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: "inspinia",
                        files: ["js/controllers/user/dictionaryCtrl.js?ver=" + verNo]
                    });
                }]
            }
        })
        .state('user.logs', {
            url: "/managerLog",
            templateUrl: "views/user/sysLog.html",
            data: {pageTitle: '日志管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/user/sysLogCtrl.js?ver=' + verNo]
                        });
                }]
            }
        })
}