
/**
 * Created by Administrator on 2019/7/3/003.
 * 积分菜单
 */
function initIntegral(stateProvider){

    stateProvider
        //---------------------业务管理---start------------------------------------
        .state('management', {
            abstract: true,
            url: "/management",
            templateUrl: "views/common/content.html"
        })
        .state('management.business', {
            url: "/business",
            templateUrl: 'views/integral/business/businessQuery.html',
            data: {pageTitle: '业务管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/business/businessQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //会员等级与权益预览
      .state('management.businessTaskPreview', {
          url: "/businessTaskPreview/:id/:businessNo",
          templateUrl: 'views/integral/business/businessTaskPreview.html',
          data: {pageTitle: '会员等级与权益预览'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('localytics.directives');
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/integral/business/businessTaskPreviewCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
        .state('management.businessDetail', {
            url: "/businessDetail/:businessNo",
            templateUrl: 'views/integral/business/businessDetail.html',
            data: {pageTitle: '业务组详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                            'js/controllers/integral/business/businessDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.businessBasic', {
            url: "/businessBasic/:businessNo",
            templateUrl: 'views/integral/business/businessBasic.html',
            data: {pageTitle: '业务组基本信息'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css',
                            'js/controllers/integral/business/businessBasicCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.businessTask', {
            url: "/businessTask/:businessNo",
            templateUrl: 'views/integral/business/businessTask.html',
            data: {pageTitle: '任务选择'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/business/businessTaskCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.businessTaskEdit', {
            url: "/businessTaskEdit/:id/:businessNo",
            templateUrl: 'views/integral/business/businessTaskEdit.html',
            data: {pageTitle: '任务修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');

                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css',
                            'js/controllers/integral/business/businessTaskEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.businessTaskDetail', {
            url: "/businessTaskDetail/:id/:businessNo",
            templateUrl: 'views/integral/business/businessTaskDetail.html',
            data: {pageTitle: '任务详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');

                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/business/businessTaskDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })


        .state('management.businessTaskDetailTwo', {
            url: "/businessTaskDetailTwo/:id/:businessNo",
            templateUrl: 'views/integral/business/businessTaskDetailTwo.html',
            data: {pageTitle: '任务详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');

                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/business/businessTaskDetailTwoCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //------

        .state('management.taskReward', {
            url: "/taskReward",
            templateUrl: 'views/integral/taskReward/taskRewardQuery.html',
            data: {pageTitle: '任务管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/taskReward/taskRewardQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })


        .state('management.addTaskRewardInfo', {
            url: "/addTaskRewardInfo",
            templateUrl: 'views/integral/taskReward/taskRewardAdd.html',
            data: {pageTitle: '任务新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css',
                            'js/controllers/integral/taskReward/taskRewardAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.editTaskRewardInfo', {
            url: "/editTaskRewardInfo/:id",
            templateUrl: 'views/integral/taskReward/taskRewardEdit.html',
            data: {pageTitle: '任务修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');

                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/integral/taskReward/taskRewardEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.taskRewardInfoDetail', {
            url: "/taskRewardInfoDetail/:id",
            templateUrl: 'views/integral/taskReward/taskRewardDetail.html',
            data: {pageTitle: '任务详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');

                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/integral/taskReward/taskRewardDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        //弹窗
        .state('management.popup', {
            url: "/popup",
            templateUrl: 'views/integral/popup/popupQuery.html',
            data: {pageTitle: '弹窗管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/popup/popupQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('management.addPopup', {
            url: "/addPopup",
            templateUrl: 'views/integral/popup/popupAdd.html',
            data: {pageTitle: '任务新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css',
                            'js/controllers/integral/popup/popupAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('management.editPopup', {
            url: "/addPopup/:id",
            templateUrl: 'views/integral/popup/popupEdit.html',
            data: {pageTitle: '任务新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css',
                            'js/controllers/integral/popup/popupEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('management.popupDetail', {
            url: "/popupDetail/:id",
            templateUrl: 'views/integral/popup/popupDetail.html',
            data: {pageTitle: '任务详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');

                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/integral/popup/popupDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })



        //--活动-
        .state('management.activityChoice', {
            url: "/activityChoice/:businessNo",
            templateUrl: 'views/integral/business/activityChoice.html',
            data: {pageTitle: '活动选择'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/business/activityChoiceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busLuckDrawpConfig', {
            url: "/busLuckDrawpConfig/:actCode/:businessNo",
            templateUrl: 'views/integral/businessActivity/busLuckDrawpConfig.html',
            data: {pageTitle: '积分抽奖'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/integral/businessActivity/busLuckDrawpConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busLuckDrawpConfigDetail', {
            url: "/busLuckDrawpConfigDetail/:actCode/:businessNo/:source",
            templateUrl: 'views/integral/businessActivity/busLuckDrawpConfigDetail.html',
            data: {pageTitle: '积分抽奖详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/integral/businessActivity/busLuckDrawpConfigDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busPrizeAdd', {
            url: "/busPrizeAdd/:businessNo/:actCode/:actDetCode",
            templateUrl: "views/integral/businessActivity/busPrizeAdd.html",
            data: {pageTitle: '新增奖品信息'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/busPrizeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busPrizeEdit', {
            url: "/busPrizeEdit/:businessNo/:awardCode",
            templateUrl: "views/integral/businessActivity/busPrizeEdit.html",
            data: {pageTitle: '编辑奖品信息'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/busPrizeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busPrizeDetail', {
            url: "/busPrizeDetail/:businessNo/:awardCode/:source",
            templateUrl: "views/integral/businessActivity/busPrizeDetail.html",
            data: {pageTitle: '奖品信息详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/busPrizeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('management.busCashMeetConfigDetail', {
            url: "/busCashMeetConfigDetail/:actCode/:businessNo/:source",
            templateUrl: 'views/integral/businessActivity/busCashMeetConfigDetail.html',
            data: {pageTitle: '会员礼遇季详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/integral/businessActivity/busCashMeetConfigDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('management.blacklistBack', {
            url: "/blacklistBack/:businessNo/:actCode/:awardCode",
            templateUrl: "views/integral/businessActivity/busPrizeBlacklistQuery.html",
            data: {pageTitle: '奖项黑名单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/busPrizeBlacklistQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.blacklistBackDetail', {
            url: "/blacklistBackDetail/:businessNo/:actCode/:awardCode/:source",
            templateUrl: "views/integral/businessActivity/busPrizeBlacklistQueryDetail.html",
            data: {pageTitle: '奖项黑名单管理详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/busPrizeBlacklistQueryDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('management.busActivityConfig', {
            url: "/busActivityConfig/:actCode/:businessNo",
            templateUrl: 'views/integral/businessActivity/busActivityConfig.html',
            data: {pageTitle: '用户激活'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/integral/businessActivity/busActivityConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busActivityConfigDetail', {
            url: "/busActivityConfigDetail/:actCode/:businessNo/:source",
            templateUrl: 'views/integral/businessActivity/busActivityConfigDetail.html',
            data: {pageTitle: '用户激活详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/integral/businessActivity/busActivityConfigDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busContentAdd', {
            url: "/busContentAdd/:businessNo/:actCode",
            templateUrl: "views/integral/businessActivity/busContentAdd.html",
            data: {pageTitle: '活动内容新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/busContentAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busContentEdit', {
            url: "/busContentEdit/:businessNo/:actDetCode",
            templateUrl: "views/integral/businessActivity/busContentEdit.html",
            data: {pageTitle: '活动内容修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/busContentEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.busContentDetail', {
            url: "/busContentDetail/:businessNo/:actDetCode/:source",
            templateUrl: "views/integral/businessActivity/busContentDetail.html",
            data: {pageTitle: '活动内容详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/busContentDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.rangeManagement', {
            url: "/rangeManagement/:businessNo/:actCode",
            templateUrl: "views/integral/businessActivity/rangeManagementQuery.html",
            data: {pageTitle: '范围管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/rangeManagementQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('management.rangeManagementDetail', {
            url: "/rangeManagementDetail/:businessNo/:actCode",
            templateUrl: "views/integral/businessActivity/rangeManagementQueryDetail.html",
            data: {pageTitle: '范围管理详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessActivity/rangeManagementQueryDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //--活动-

        //--积分查询
        .state('management.scoreAccount', {
            url: "/scoreAccount",
            templateUrl: 'views/integral/scoreAccount/scoreAccountQuery.html',
            data: {pageTitle: '积分发行查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/scoreAccount/scoreAccountQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //--业务流水查询
        .state('management.businessFlow', {
            url: "/businessFlow",
            templateUrl: 'views/integral/businessFlow/businessFlowQuery.html',
            data: {pageTitle: '业务流水查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/businessFlow/businessFlowQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //---------------------业务管理---start------------------------------------


        //-----------------------------会员------start-------------------------------------
        .state('leaguer', {
            abstract: true,
            url: "/leaguer",
            templateUrl: "views/common/content.html"
        })
        .state('leaguer.leaguerList', {
            url: "/leaguerList",
            templateUrl: 'views/integral/leaguer/leaguerQuery.html',
            data: {pageTitle: '会员查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/leaguer/leaguerQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('leaguer.leaguerDetail', {
            url: "/leaguerDetail/:id/:businessNo",
            templateUrl: 'views/integral/leaguer/leaguerDetail.html',
            data: {pageTitle: '会员详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integral/leaguer/leaguerDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
    //-----------------------------会员----end---------------------------------------

    // ---------------------会员活动---start-----------------------------------------
        .state('activities', {
            abstract: true,
            url: "/activities",
            templateUrl: "views/common/content.html"
        })
        .state('activities.activityManagement', {
            url: "/activityManagement",
            templateUrl: 'views/activity/activityManagement/activityManagementQuery.html',
            data: {pageTitle: '会员管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/activityManagementQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.luckDrawpConfig', {
            url: "/luckDrawpConfig/:actCode/:businessNo",
            templateUrl: 'views/activity/activityManagement/luckDrawpConfig.html',
            data: {pageTitle: '积分抽奖'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/activity/activityManagement/luckDrawpConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.luckDrawpConfigDetail', {
            url: "/luckDrawpConfigDetail/:actCode/:businessNo",
            templateUrl: 'views/activity/activityManagement/luckDrawpConfigDetail.html',
            data: {pageTitle: '积分抽奖详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/activity/activityManagement/luckDrawpConfigDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.cashMeetConfig', {
            url: "/cashMeetConfig/:actCode/:businessNo/:source",
            templateUrl: 'views/activity/activityManagement/cashMeetConfig.html',
            data: {pageTitle: '会员现金礼遇季'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/activity/activityManagement/cashMeetConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.cashMeetConfigDetail', {
            url: "/cashMeetConfigDetail/:actCode/:businessNo/:source",
            templateUrl: 'views/activity/activityManagement/cashMeetConfigDetail.html',
            data: {pageTitle: '会员现金礼遇季'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/activity/activityManagement/cashMeetConfigDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.cashMeetAfterConditionDetail', {
            url: "/cashMeetAfterConditionDetail/:actCode/:actDetCode/:business/:source",
            templateUrl: "views/activity/activityManagement/cashMeetAfterConditionDetail.html",
            data: {pageTitle: '后置条件详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/cashMeetAfterConditionDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.cashMeetAfterConditionUpdate', {
            url: "/cashMeetAfterConditionUpdate/:actCode/:actDetCode/:business/:source",
            templateUrl: "views/activity/activityManagement/cashMeetAfterConditionUpdate.html",
            data: {pageTitle: '后置条件修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/cashMeetAfterConditionConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.activityContentAdd', {
            url: "/activityContentAdd/:actCode",
            templateUrl: "views/activity/activityManagement/cashMeetActivityContentAdd.html",
            data: {pageTitle: '新增活动属性'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/cashMeetActivityContentAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.activityContentDetail', {
            url: "/activityContentDetail/:actCode/:actDetCode/:business/:source",
            templateUrl: "views/activity/activityManagement/cashMeetActivityContentDetail.html",
            data: {pageTitle: '活动内容详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/cashMeetActivityContentDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.activityContentConfig', {
            url: "/activityContentConfig/:actCode/:actDetCode/:business/:source",
            templateUrl: "views/activity/activityManagement/cashMeetActivityContentUpdate.html",
            data: {pageTitle: '活动内容修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/cashMeetActivityContentConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.activityBlackQuery', {
            url: "/activityBlackQuery/:actDetCode",
            templateUrl: 'views/activity/activityManagement/activityBlackQuery.html',
            data: {pageTitle: '黑名单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/activityBlackListQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.activityLeagerBagSerial', {
            url: "/activityLeagerBagSerial",
            templateUrl: 'views/activity/activityManagement/leagerBagSerialQuery.html',
            data: {pageTitle: '全民返现记录'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/leagerBagSerialQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.prizeAdd', {
            url: "/prizeAdd/:actCode/:actDetCode",
            templateUrl: "views/activity/activityManagement/prizeAdd.html",
            data: {pageTitle: '新增奖品信息'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/prizeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.prizeEdit', {
            url: "/prizeEdit/:awardCode",
            templateUrl: "views/activity/activityManagement/prizeEdit.html",
            data: {pageTitle: '编辑奖品信息'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/prizeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.prizeDetail', {
            url: "/prizeDetail/:awardCode/:source",
            templateUrl: "views/activity/activityManagement/prizeDetail.html",
            data: {pageTitle: '奖品信息详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/prizeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('activities.activityConfig', {
            url: "/activityConfig/:actCode/:businessNo",
            templateUrl: 'views/activity/activityManagement/activityConfig.html',
            data: {pageTitle: '用户激活'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/activity/activityManagement/activityConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.activityConfigDetail', {
            url: "/activityConfigDetail/:actCode/:businessNo",
            templateUrl: 'views/activity/activityManagement/activityConfigDetail.html',
            data: {pageTitle: '用户激活详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/plugins/summernote/summernote.min.js','js/plugins/summernote/angular-summernote.min.js',
                            'css/plugins/summernote/summernote.css','js/controllers/activity/activityManagement/activityConfigDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.contentAdd', {
            url: "/contentAdd/:actCode",
            templateUrl: "views/activity/activityManagement/contentAdd.html",
            data: {pageTitle: '活动内容新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/contentAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.contentEdit', {
            url: "/contentEdit/:actDetCode",
            templateUrl: "views/activity/activityManagement/contentEdit.html",
            data: {pageTitle: '活动内容修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/contentEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.contentDetail', {
            url: "/contentDetail/:actDetCode/:source",
            templateUrl: "views/activity/activityManagement/contentDetail.html",
            data: {pageTitle: '活动内容详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityManagement/contentDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.winningRecord', {
            url: "/winningRecord",
            templateUrl: 'views/activity/winningRecord/winningRecordQuery.html',
            data: {pageTitle: '抽奖记录查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/winningRecord/winningRecordQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('activities.activityCensus', {
            url: "/activityCensus",
            templateUrl: 'views/activity/activityCensus/activityCensusQuery.html',
            data: {pageTitle: '活动数据统计'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityCensus/activityCensusQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
    //---------------------会员活动---end------------------------------------------

    //---------------------积分商城---start----------------------------------------
        .state('integralMall', {
            abstract: true,
            url: "/integralMall",
            templateUrl: "views/common/content.html"
        })
        .state('integralMall.goodsManagement', {
            url: "/goodsManagement",
            templateUrl: 'views/integralMall/goodsManagement/goodsManagementQuery.html',
            data: {pageTitle: '物品管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/integralMall/goodsManagement/goodsManagementQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
    //---------------------积分商城---end-------------------------------------------

}