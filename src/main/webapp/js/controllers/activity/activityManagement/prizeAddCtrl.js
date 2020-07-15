/**
 * 奖项新增
 */
angular.module('inspinia',['angularFileUpload']).controller('prizeAddCtrl',function($scope,$http,i18nService,$state,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.actCode=$stateParams.actCode;
    $scope.actDetCode=$stateParams.actDetCode;

    $scope.rewardTypeSelect = angular.copy($scope.rewardTypeList);
    $scope.rewardTypeSelect.unshift({text:"请选择",value:""});
    $scope.rewardTypeSelect.push({text:"未中奖",value:"99"});
    $scope.rewardTypeStr=angular.toJson($scope.rewardTypeSelect);

    $scope.addInfo={actCode:$stateParams.actCode,actDetCode:$stateParams.actDetCode,prize:{rewardType:"",calcType:""}};
    $scope.conList=[];

    $scope.cleanSta=function(){
        $scope.conSta=false;//卷
        $scope.moneySta=false;//金额
        $scope.numSta=false;//数值
        $scope.integralSta=false;//积分有效期
        $scope.addInfo.prize.awardNum="";
        $scope.addInfo.prize.expireDay="";
        $scope.addInfo.prize.calcType="";
    };

    $scope.awardTypeChange=function(){
        var sta=$scope.addInfo.prize.rewardType;
        $scope.cleanSta();
        if(sta==null||sta==""){
            return;
        }
        if(sta=="1"){//会员积分
            $scope.numSta=true;
            $scope.integralSta=true;
            $scope.addInfo.prize.calcType="1";
        }else if(sta=="2" ||sta=="3"){
            $scope.numSta=true;
            $scope.addInfo.prize.calcType="1";
        }else if(sta=="4" ||sta=="5" ||sta=="6"){//卷
            $scope.getCouponList(sta);
            $scope.conSta=true;
        }else if(sta=="7"){//现金
            $scope.moneySta=true;
            $scope.addInfo.prize.calcType="1";
        }
    };
    $scope.awardTypeChange();
    //券列表
    $scope.getCouponList=function(actCode){
        $scope.conList=[{text:"请选择",value:""}];
    };

    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>500*1024){
                $scope.notice("上传图片太大了!");
                return false;
            }
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.saveInfo(response.url);
        }
    };

    $scope.submitting = false;
    //新增
    $scope.commitInfo = function(){
        if($scope.submitting){
            return;
        }
        if(uploaderImg.queue.length<1){
            $scope.notice("图片不能为空!");
            return;
        }
        if(Number($scope.addInfo.awardOdds)<0||Number($scope.addInfo.awardOdds)>=100){
            $scope.notice("中奖概率只能在大于等于零小于100之间!");
            return;
        }
        if($scope.addInfo.prize.rewardType==null||$scope.addInfo.prize.rewardType==""){
            $scope.notice("奖品类型不能为空!");
            return ;
        }else{
            if($scope.addInfo.prize.rewardType=="1"){
                if($scope.addInfo.prize.expireDay==null||$scope.addInfo.prize.expireDay==""){
                    $scope.notice("有效期不能为空!");
                    return ;
                }else{
                    var reg=/^[1-9]\d*$/;
                    if(!reg.test($scope.addInfo.prize.expireDay)){
                        $scope.notice("有效期格式不对!");
                        return ;
                    }
                }
            }
        }
        if($scope.addInfo.prize.awardNum!=null&&$scope.addInfo.prize.awardNum!=""){
            if($scope.addInfo.prize.rewardType=="7"){
                if(Number($scope.addInfo.prize.awardNum)<=0){
                    $scope.notice("金额不能小于等于0!");
                    return ;
                }
            }
        }
        $scope.submitting = true;
        uploaderImg.uploadAll();// 上传消息图片
    };
    $scope.saveInfo=function(url){
        $scope.infoSub = angular.copy($scope.addInfo);
        $scope.infoSub.awardImg=url;
        $http.post("activityManagement/addLuckPrizeTemplate",
            "info="+angular.toJson($scope.infoSub),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.goBackFun();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };

    //返回
    $scope.goBackFun=function(){
        window.open('welcome.do#/activities/luckDrawpConfig/'+$scope.actCode+'/0', '_self');
    };
});