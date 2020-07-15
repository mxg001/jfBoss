/**
 * 弹窗新增
 */
angular.module('inspinia',['angularFileUpload']).controller('popupEditCtrl',function($scope,$http,i18nService,$state,SweetAlert,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.addInfo = {businessNo:'0',status:0,serviceNo:'',
        popStartTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        popEndTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};

    $scope.showSelects = [{name:'APP首页',id:1,checkStatus:0},{name:'会员首页',id:2,checkStatus:0}];

    //获取数据
    $http.post("pop/getPopupConfig","id="+$stateParams.id,
        {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addInfo=data.info;
                $scope.addInfo.condition = "满足当前活动参与条件";

                $scope.addInfo.popStartTime = moment($scope.addInfo.popStartTime).format('YYYY-MM-DD HH:mm:ss');
                $scope.addInfo.popEndTime = moment($scope.addInfo.popEndTime).format('YYYY-MM-DD HH:mm:ss');

                var position = $scope.addInfo.popPosition.split(",");
                if(position.length > 0){
                    angular.forEach($scope.showSelects, function (item) {
                        for (var i = 0; i < position.length; i++) {
                            if(position[i] == item.id){
                                item.checkStatus = 1;
                            }
                        }
                    });
                }
            }
        }).error(function(data){
        $scope.notice(data.msg);
    });

    $scope.businessNoList=[];
    $http.post("businessInfoAction/getBusinessInfoListByUser",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                //$scope.businessNoList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.businessNoList.push({value:list[i].businessNo, text:list[i].businessName + " " + list[i].businessNo});
                    }
                }
            }
        });




    $scope.activityList = [];
    //活动下拉列表
    $scope.selectAllActivity = function(){
        $http.post("activityManagement/selectAllActivity?businessNo="+$scope.addInfo.businessNo)
            .success(function(result){
                //响应成功
                for(var i=0; i<result.length; i++){
                    $scope.activityList.push({value:result[i].actCode,text:result[i].actName + " " + result[i].actCode});
                }
            });
    }
    $scope.selectAllActivity();


    //上传图片,定义控制器路径
    $scope.uploaderImg1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    $scope.uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>20*1024*1024){
                $scope.notice("上传图片太大了!");
                return false;
            }
            return this.queue.length < 1;
        }
    });
    //过滤格式
    $scope.uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderImg1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.popImg = response.url;
        }
    };

    $scope.submitting = false;
    //新增提交
    $scope.editInfoSubmit = function(){

        if($scope.addInfo.businessNo == '0'){
            $scope.notice("请选择关联业务组");
            return;
        }
        if($scope.addInfo.serviceNo == ''){
            $scope.notice("请选择关联活动");
            return;
        }

        $scope.addInfo.popPosition = "";
        angular.forEach($scope.showSelects, function(item){
            if(item.checkStatus==1){
                $scope.addInfo.popPosition = $scope.addInfo.popPosition + item.id + ",";
            }
        });
        if(!$scope.addInfo.popPosition){
            $scope.notice("弹窗展示页面不能为空");
            return;
        }else{
            var len = $scope.addInfo.popPosition.length;
            $scope.addInfo.popPosition = $scope.addInfo.popPosition.substring(0, len-1);
        }

        if($scope.submitting){
            return;
        }
        $scope.submitting = true;

        $scope.addInfo.popImgUrl="";
        $http.post("pop/add","info=" + angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(result){
                if(result.status){
                    $scope.notice("修改成功");
                    $state.transitionTo('management.popup',null,{reload:true});
                }else{
                    $scope.notice(result.msg);
                }
                $scope.submitting = false;
            })
            .error(function(result){
                $scope.submitting = false;
                $scope.notice(result.msg);
            });

    };

    /**
     *富文本框按钮控制
     */
    $scope.summeroptions = {
        toolbar: [
            ['style', ['bold', 'italic', 'underline','clear']],
            ['fontface', ['fontname']],
            ['textsize', ['fontsize']],
            ['fontclr', ['color']],
            ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
            ['height', ['height']],
            ['insert', ['hr']],
            // ['insert', ['link','picture','video','hr']],
            ['view', ['codeview']]
        ]
    };
});