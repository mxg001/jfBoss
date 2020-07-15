/**
 * 任务修改
 */
angular.module('inspinia',['angularFileUpload']).controller('taskRewardEditCtrl',function($scope,$http,i18nService,$state,$stateParams,SweetAlert,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //值类型
    $scope.calcTypeSelect = [{text:"数值",value:"1"},{text:"公式",value:"2"}];
    $scope.calcTypeStr=angular.toJson(angular.copy($scope.calcTypeSelect));

    //权益类型
    $scope.rewardTypeSelect = angular.copy($scope.rewardTypeList);
    $scope.rewardTypeStr=angular.toJson($scope.rewardTypeSelect);

    //频率类型
    $scope.frequencyTypeSelect = angular.copy($scope.frequencyTypeList);
    $scope.frequencyTypeSelect.unshift({value:"",text:"请选择"});

    $scope.addInfo={taskType:""};
    $scope.taskRewardConfigList=[];//权益列表
    //流水下发方式
    $scope.grantTypeSelect = [{value:null,text:"请选择"}];
    for(var i=0; i<$scope.grantTypeList.length; i++){
        $scope.grantTypeSelect.push({value:$scope.grantTypeList[i].value, text:$scope.grantTypeList[i].text});
    }

    //触发类型
    $scope.triggerTypeSelect = [{value:"",text:"请选择"}];
    for(var i=0; i<$scope.triggerTypeList.length; i++){
        $scope.triggerTypeSelect.push({value:$scope.triggerTypeList[i].value, text:$scope.triggerTypeList[i].text});
    }

    //获取任务类型
    $scope.taskTypeList=[];
    $http.post("taskRewardAction/getTaskTypeList")
        .success(function(data){
            if(data.status){
                $scope.taskTypeList.push({value:"",text:"请选择"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.taskTypeList.push({value:list[i].taskTypeNo, text:list[i].taskTypeName});
                    }
                }
            }
        });

    //触发类型变更事件
    $scope.showTransFormula = false;
    $scope.taskParamsChange = function(){
        if($scope.addInfo.taskParams=="trans"){
            $scope.showTransFormula = true;
            $scope.calcTypeSelect = [{text:"数值",value:"1"},{text:"公式",value:"2"}];
        }else{
            $scope.showTransFormula = false;
            $scope.calcTypeSelect = [{text:"数值",value:"1"}];
        }
    }
    //任务类型改变事件
    $scope.taskTypeChange = function(init){
        if($scope.addInfo.taskType=='TYPE002'){
            if(!init){
                $scope.addInfo.restrictNum=1;
            }
            $("#restrictNum").attr("readonly","true");
        }else{
            if(!init){
                $scope.addInfo.restrictNum = "";
            }
            $("#restrictNum").removeAttr("readonly");
        }
    };
    //流水下发方式
    $scope.grantTypeChange = function(init){
        if($scope.addInfo.grantType=='1'){
            if(!init) {
                $scope.addInfo.restrictFrequency = 1;
            }
            $("#restrictFrequency").attr("readonly","true");
        }else{
            if(!init) {
                $scope.addInfo.restrictFrequency = "";
            }
            $("#restrictFrequency").removeAttr("readonly");
        }
    };

    //获取数据
    $http.post("taskRewardAction/getTaskRewardInfo","id="+$stateParams.id,
        {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addInfo=data.info;
                $scope.taskParamsChange();
                $scope.taskTypeChange(true);
                $scope.grantTypeChange(true);
                $scope.addInfo.frequencyType="3";
                $scope.showImg=$scope.addInfo.imgUrl;
                $scope.taskRewardConfigList=data.info.taskRewardConfigList;
                //数据回显转换
                if($scope.taskRewardConfigList!=null&&$scope.taskRewardConfigList.length>0){
                    angular.forEach($scope.taskRewardConfigList,function(item){
                        if(item.calcType == "1"){
                            item.rewardTemplateStr=item.rewardTemplate;
                        }else{
                            var strs=item.rewardTemplate.split(",");
                            if(strs.length==2){
                                item.x=strs[0];
                                item.y=strs[1];
                                item.rewardTemplateStr="[触发类型值mod("+item.x+")]*"+item.y;
                            }
                        }
                    })
                }
            }
        }).error(function(data){
            $scope.notice(data.msg);
         });

    $scope.userGrid={                           //配置表格
        data: 'taskRewardConfigList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'rewardName',displayName:'权益名称',width:150},
            { field: 'rewardType',displayName:'赠送类型',width:150,cellFilter:"formatDropping:" +  $scope.rewardTypeStr},
            { field: 'calcType',displayName:'值类型',width:150,cellFilter:"formatDropping:" +  $scope.calcTypeStr},
            { field: 'rewardTemplateStr',displayName:'数量',width:250},
            { field: 'rewardEffeDay',displayName:'有效期',width:100},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a ng-click="grid.appScope.updateModal(row)">修改</a> ' +
            '<a ng-click="grid.appScope.deleteAddInfo(row,rowRenderIndex)"> | 删除</a> ' +
            '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
    $scope.editSta=false;
    $scope.addModal = function(){
        if($scope.addInfo.taskParams==null || $scope.addInfo.taskParams==""){
            $scope.notice("请选择触发类型!");
            return;
        }
        $scope.addInfoEntry={rewardType:"1",calcType:"1"};
        $scope.rewardTypeChange();
        $scope.calcTypeChange();
        $scope.editSta=false;
        $('#addInfoModal').modal('show');
    };
    $scope.hideModal= function(){
        $('#addInfoModal').modal('hide');
    };

    //赠送类型 变更事件
    $scope.rewardTypeChange=function (updateCalcType) {
        $scope.oldCalcTypeSelect = angular.copy($scope.calcTypeSelect);
        if($scope.addInfoEntry.rewardType=="1"){
            $scope.rewardTypeSta=true;
        }else{
            $scope.rewardTypeSta=false;
        }
        if($scope.addInfoEntry.rewardType==4 || $scope.addInfoEntry.rewardType==5 || $scope.addInfoEntry.rewardType==6){
            $scope.calcTypeSelect = [{"value":null,"text":"请选择"}];
            $scope.showRewardSelectList = true;
            $scope.addInfoEntry.calcType = null;
            $scope.calcTypeSta = true;
        }else{
            $scope.calcTypeSelect = angular.copy($scope.oldCalcTypeSelect);
            if(!updateCalcType){
                $scope.addInfoEntry.calcType = "1";
                $scope.calcTypeChange();
            }
            $scope.showRewardSelectList = false;
        }
    };
    //值类型类型 变更事件
    $scope.calcTypeChange=function () {
        if($scope.addInfoEntry.calcType=="1"){
            $scope.calcTypeSta=true;
        }else{
            $scope.calcTypeSta=false;
        }
    };

    $scope.saveEntry=function (sta) {
        if($scope.addInfoEntry.rewardType=="1"){
            if($scope.addInfoEntry.rewardEffeDay==null||$scope.addInfoEntry.rewardEffeDay==""){
                $scope.notice("有效期不能为空!");
                return;
            }else{
                if(Number($scope.addInfoEntry.rewardEffeDay)>0){
                    var reg=/^[1-9]\d*$/;
                    if(!reg.test(Number($scope.addInfoEntry.rewardEffeDay))){
                        $scope.notice("有效期格式不对!");
                        return;
                    }
                }else{
                    $scope.notice("有效期格式不对!");
                    return;
                }
            }
        }else{
            $scope.addInfoEntry.rewardEffeDay=null;
        }
        if($scope.addInfoEntry.calcType=="1"){
            if(Number($scope.addInfoEntry.num)>0){
                var reg="";
                if($scope.addInfoEntry.rewardType=="7"){
                    var reg=/^([1-9]\d{0,9}|0)(\.\d{1,2})?$/;
                }else{
                    var reg=/^[1-9][0-9]*$/;
                }
                if(!reg.test(Number($scope.addInfoEntry.num))){
                    $scope.notice("数量格式不对!");
                    return;
                }
            }else{
                $scope.notice("数量格式不对!");
                return;
            }
            $scope.addInfoEntry.rewardTemplate=$scope.addInfoEntry.num;
            $scope.addInfoEntry.rewardTemplateStr=$scope.addInfoEntry.num;
            $scope.addInfoEntry.rewardTemplateFormula=$scope.addInfoEntry.num;

        }else{
            if(Number($scope.addInfoEntry.x)>0&&Number($scope.addInfoEntry.y)>0){
                var reg=/^[1-9]\d*$/;
                if(!reg.test(Number($scope.addInfoEntry.x)) || !reg.test(Number($scope.addInfoEntry.y))){
                    $scope.notice("数量格式不对!");
                    return;
                }
            }else{
                $scope.notice("数量格式不对!");
                return;
            }
            $scope.addInfoEntry.rewardTemplate=$scope.addInfoEntry.x+","+$scope.addInfoEntry.y;
            $scope.addInfoEntry.rewardTemplateStr="[触发类型值mod("+$scope.addInfoEntry.x+")]*"+$scope.addInfoEntry.y;
            $scope.addInfoEntry.rewardTemplateFormula="{0}mod"+$scope.addInfoEntry.x+"*"+$scope.addInfoEntry.y;

        }
        if(sta==1){
            $scope.taskRewardConfigList.push({
                rewardName:$scope.addInfoEntry.rewardName,
                rewardType:$scope.addInfoEntry.rewardType,
                calcType:$scope.addInfoEntry.calcType,
                rewardTemplateStr:$scope.addInfoEntry.rewardTemplateStr,
                rewardTemplateFormula:$scope.addInfoEntry.rewardTemplateFormula,
                rewardTemplate:$scope.addInfoEntry.rewardTemplate,
                rewardEffeDay:$scope.addInfoEntry.rewardEffeDay
            });
        }else if(sta==2){
            //修改
            $scope.oldRowEntity.rewardName = $scope.addInfoEntry.rewardName;
            $scope.oldRowEntity.rewardType = $scope.addInfoEntry.rewardType;
            $scope.oldRowEntity.calcType = $scope.addInfoEntry.calcType;
            $scope.oldRowEntity.rewardTemplateStr = $scope.addInfoEntry.rewardTemplateStr;
            $scope.oldRowEntity.rewardTemplateFormula = $scope.addInfoEntry.rewardTemplateFormula;
            $scope.oldRowEntity.rewardTemplate = $scope.addInfoEntry.rewardTemplate;
            $scope.oldRowEntity.rewardEffeDay = $scope.addInfoEntry.rewardEffeDay;
        }

        $scope.hideModal();
    };


    //编辑
    $scope.updateModal = function(row){
        if($scope.addInfo.taskParams==null || $scope.addInfo.taskParams==""){
            $scope.notice("请选择触发类型!");
            return;
        }
        $scope.addInfoEntry=angular.copy(row.entity);
        $scope.oldRowEntity = row.entity;

        if($scope.addInfo.taskParams!="trans"){
            if($scope.addInfoEntry.rewardType!=4 && $scope.addInfoEntry.rewardType!=5 && $scope.addInfoEntry.rewardType!=6){
                if($scope.addInfoEntry.calcType!="1"){
                    $scope.notice("当前触发类型不能含有公式类型的业务配置!");
                    return;
                }
            }
        }
        $scope.rewardTypeChange($scope.addInfoEntry.calcType);
        $scope.calcTypeChange();
        $scope.initAddInfo();
        $scope.editSta=true;

        $('#addInfoModal').modal('show');
    };
    $scope.initAddInfo=function(){
        if(!$scope.addInfoEntry.rewardType=="1"){
            $scope.addInfoEntry.rewardEffeDay==null;
        }
        if($scope.addInfoEntry.calcType=="1"){
            $scope.addInfoEntry.num=$scope.addInfoEntry.rewardTemplate;
        }else{
            var strs=$scope.addInfoEntry.rewardTemplate.split(",");
            $scope.addInfoEntry.x=strs[0];
            $scope.addInfoEntry.y=strs[1];
        }
    };

    $scope.deleteAddInfo=function (row,index) {
        SweetAlert.swal({
                title: "确定删除吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.taskRewardConfigList.splice(index, 1);
                }
            });
    };
    //上传图片,定义控制器路径
    $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    $scope.uploaderImg.filters.push({
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
    $scope.uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.imgUrl = response.url;
        }
    };

    $scope.submitting = false;
    //新增提交
    $scope.addInfoSubmit = function(){
        if($scope.addInfo.restrictNum!=null){
            if($scope.addInfo.restrictNum!=-1&&Number($scope.addInfo.restrictNum)<0){
                $scope.notice("可调用次数格式不符!");
                return;
            }
        }
        if($scope.taskRewardConfigList==null||$scope.taskRewardConfigList.length<=0){
            $scope.notice("业务配置不能为空!");
            return;
        }

        if($scope.uploaderImg.queue.length<1){
            $scope.addInfo.imgUrl=null;
        }else{
            if($scope.addInfo.imgUrl==null || $scope.addInfo.imgUrl==""){
                $scope.notice("任务图标不能为空!");
                return;
            }
        }

        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $scope.addInfoSub=angular.copy($scope.addInfo);
        $scope.addInfoSub.taskOverview=null;
        $scope.addInfoSub.taskRewardConfigList=$scope.taskRewardConfigList;

        var data={
            info:angular.toJson($scope.addInfoSub),
            taskOverview:$scope.addInfo.taskOverview
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("taskRewardAction/editTaskRewardInfo",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('management.taskReward',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
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