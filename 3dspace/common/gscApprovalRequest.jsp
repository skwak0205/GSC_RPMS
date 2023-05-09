<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <!-- loader  --> 
    <div class="" id="div_load_image" style="display: flex; position: absolute;width: 100%; height: 100%; top: 0; background-color: transparent; align-items: center; z-index: 9999; justify-content: center;" > 
        <!-- waiting gif -->
        <div id="div_load_image2" class="wait4running" > 
            <img src="images/loading.gif" style="width:100px; height:100px;"> 
        </div>
    </div>

    <meta charset="UTF-8" />
    <title>결재요청</title>
    <link rel="stylesheet" type="text/css" href="styles/gsccommon.css" />
    <link rel="stylesheet" type="text/css" href="styles/gscaltex.css" />
    <script src="scripts/jquery.min.js"></script>
    <script src="scripts/kendo.all.js"></script>
    <script src="scripts/kendo.all.min.js"></script>
    <script src="scripts/kendoUtils.js"></script>
    <script src="scripts/kendoTrans.js"></script>
</head>

<body style="padding-left: 3%; overflow: scroll">
<h2 id="ph" style="font-size: 40px; padding-left: 42%">결재요청</h2>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div id="parent" class="box-body">
                <form id="formCond" name="formCond" method="post">
                    <div id="condition" class="condition" style="width: 94%">
                        <table>
                            <col style="width: 10%" />
                            <col style="width: 0" />
                            <col style="width: 5%" />
                            <tbody>
                            <tr>
                                <td><label>제목</label></td>
                                <td><input id="title" class="k-input" /></td>
                                <td>
                                    <button
                                            class="k-button k-button-solid-primary k-button-solid k-button-md k-rounded-md"
                                            id="requestApproval"
                                            type="button"
                                            style="margin-top: 2em; float: right"
                                    >
                                        결재요청
                                    </button>
                                    <button
                                            class="k-button k-button-solid-primary k-button-solid k-button-md k-rounded-md"
                                            id="requestApprovalTEST"
                                            type="button"
                                            style="margin-top: 2em; float: right; display: none;"
                                    >
                                        고객사 결제 테스트
                                    </button>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <label for="approver">결재자</label>
                                </td>
                                <td>
                                    <input id="approver" placeholder="Select approver..." />
                                </td>
                                <td>
                                    <button
                                            class="k-button k-button-solid-primary k-button-solid k-button-md k-rounded-md"
                                            id="addApprover"
                                            type="button"
                                            style="margin-top: 2em; float: right"
                                    >
                                        결재추가
                                    </button>
                                </td>
                            </tr>

                            <tr>
                                <td><label for="receiver">수신/참조</label></td>
                                <td>
                                    <input id="receiver" placeholder="Select receiver..." />
                                </td>
                                <td>
                                    <button
                                            class="k-button k-button-solid-primary k-button-solid k-button-md k-rounded-md"
                                            id="addReceiver"
                                            type="button"
                                            style="margin-top: 2em; float: right"
                                    >
                                        수신추가
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div id="box" class="box">
                <div id="parent" calss="box-body">
                    <div
                            id="splitter1"
                            style="display: flex; width: 94%; height: 350px"
                    >
                        <div id="gridApprover" style="width: 50%"></div>
                        <div id="gridReceiver" style="width: 50%"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12" style="width: 94%">
            <table>
                <tbody>
                <tr>
                    <td>
                        <input id="approvalcontent" class="k-input" style="height: 60px" />
                    </td>
                </tr>
                </tbody>
            </table>
            <div id="attachmentDiv">첨부</div>
        </div>
    </div>
</section>

<form
        id="requestApprovalForm"
        method="post"
        style="margin: 0px; display: none"
        accept-charset="euc-kr"
        onsubmit="return fnSubmit(this)"
>
    <tbody id="tbodyDraft">
    <tr>
        <td>sys</td>
        <td colspan="3"><input type="text" name="sys" value="RPMS" /></td>
    </tr>
    <tr>
        <td>co_id</td>
        <td><input type="text" name="co_id" value="C1" /></td>
        <td>create_emp_id</td>
        <td><input type="text" name="create_emp_id" /></td>
    </tr>
    <tr>
        <td>class_id</td>
        <td><input type="text" name="class_id" value="2" /></td>
        <td>form_id</td>
        <td><input type="text" name="form_id" value="428" /></td>
    </tr>
    <tr>
        <td>v01</td>
        <td colspan="3">
            <textarea name="v01" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v02</td>
        <td colspan="3">
            <textarea name="v02" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v03</td>
        <td colspan="3">
            <textarea name="v03" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v04</td>
        <td colspan="3">
            <textarea name="v04" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v05</td>
        <td colspan="3">
            <textarea name="v05" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v06</td>
        <td colspan="3">
            <textarea name="v06" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v07</td>
        <td colspan="3">
            <textarea name="v07" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v08</td>
        <td colspan="3">
            <textarea name="v08" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v09</td>
        <td colspan="3">
            <textarea name="v09" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>v10</td>
        <td colspan="3">
            <textarea name="v10" rows="2" cols="57"></textarea>
        </td>
    </tr>
    <tr>
        <td>
            <input
                    id="requestApprovalFormSubmitButton"
                    type="submit"
                    value=" 전 송 "
                    style="
                background-color: WhiteSmoke;
                font-family: 맑은 고딕;
                font-size: medium;
              "
            />
        </td>
    </tr>
    </tbody>
</form>
</body>
</html>

<style></style>

<script>
    // 전역 변수 - 테스트 데이터 조회
    var TESTDATA = "";

    // 전역 변수 - 공통 데이터 처리 용도
    var urlParams = "";
    var projectInfo = {};
    var myProfile = { name: "", deptcode: "" };
    var gsCommonData = {};
    var data_Person = {};
    var data_BusinessUnit = {};

    // 전역 변수 - Grid 용도
    var gridApprover = null;
    var gridReceiver = null;
    var gridApprover_dataSource = null;
    var gridReceiver_dataSource = null;

    // 전역 변수 - 조건 처리 용도
    var ChkGetData = {myProfile: false, person: false, businessUnit: false};
    var ChkSetGridData = false;
    var ChkCount = 0;

    // 전역 변수 - 결재 url 설정
    var toasURL = "";

    $(document).ready(function () {
        WAIT.startLoader();

        // Param 처리 - 과제 처리
        fnSetParam();

        // Data 조회 및 입력
        fnSetData();

        // form 초기화 및 데이터 입력
        fnSetForm();

        // button 초기화 및 기능 선언
        fnSetButton();

        // grid Title 설정
        SetGrid.Title("gridApprover", ".k-toolbar.k-grid-toolbar", "결재자");
        SetGrid.Title("gridReceiver", ".k-toolbar.k-grid-toolbar", "수신/참조");

        // 필수값 체크
        fnChkValue();

        // 조건에 따른 Grid 기본값 설정 - fnSetGridData 하지 않은 경우, 계속 반복
        fnSetGridData();

        WAIT.stopLoader();
        
    });

    // Param 처리 - 과제 처리
    const fnSetParam = () => {
        // 현재 URL 가져오기
        var currentUrl = window.location.href;

        // URL 매개 변수 가져오기
        urlParams = new URLSearchParams(window.location.search);
        console.log("fnGetParam - urlParams:",urlParams);

        // urlParams 에서 projectId 설정
        projectInfo.projectId = urlParams.get("projectId");

        // toasURL 처리 - 개발, 운영
        if(window.location.hostname === "djrpmsdev.gscaltexdev.co.kr"){
            toasURL = "http://toasdev.gscaltexdev.co.kr/EdocOpenFromExternalSystem.aspx";
        }else if(window.location.hostname === "rpms.gscaltex.co.kr"){
            toasURL = "http://toas.gscaltex.co.kr/EdocOpenFromExternalSystem.aspx";
        }else{
            toasURL = "http://toasdev.gscaltexdev.co.kr/EdocOpenFromExternalSystem.aspx";
        }
    };

    const fnSetProject = async () => {
        $.ajax({
            type: "get",
            url:
                "/3dspace/resources/v1/modeler/gscprojects/" +
                projectInfo.projectId +
                "?$fields=none,name,objectId&$include=none",
            dataType: "json",
            success: function (data) {
                console.log("fnSetProject - data:", data);
                projectInfo.projectName = data.data[0].dataelements.name;
                projectInfo.projectObjectId = data.data[0].dataelements.objectId;

                // 새 div 요소 생성
                var newDiv = document.createElement("div");

                // 요소에 내용 추가
                newDiv.innerHTML =
                    "<a href='/widget/#/project/summary/" +
                    projectInfo.projectId +
                    "/" +
                    projectInfo.projectObjectId +
                    "/" +
                    projectInfo.projectName +
                    "' target='_blank' style='font-size: 20px;'>" +
                    projectInfo.projectName +
                    "</a>";

                // 부모 요소에 새로운 요소 추가
                var parent = document.getElementById("attachmentDiv");
                parent.appendChild(newDiv);
            },
            error: function (request, status, error) {
                console.log("Ajax 프로젝트 정보 조회 Error - request,status,error:", request, status, error);

                alert("프로젝트 정보를 가져올 수 없습니다.\n관리자에게 문의바랍니다.");
            },
        });
    };

    // Data 처리
    const fnSetData = async () => {
        // 필수 정보 조회
        getMyProfile()  // 로그인 사용자 정보 조회
        getPerson() // 전체 유저 정보 조회
        getBusinessUnit() // 전체 부서 정보 조회

        // Data 초기화 작업
        var approver_dataSource = new kendo.data.DataSource({
            data: null,
        });

        var approver_dataSource4Search = new kendo.data.DataSource({
            data: null,
        });

        var receiver_dataSource = new kendo.data.DataSource({
            data: null,
        });

        var receiver_dataSource4Search = new kendo.data.DataSource({
            data: null,
        });

        var _ddlData_ApproverType = [
            { value: "A", displayValue: "결재", },
            { value: "C", displayValue: "합의", },
            { value: "Y", displayValue: "확인", },
        ];

        var _ddlData_ReceiverType = [
            { value: "V", displayValue: "수신", },
            { value: "F", displayValue: "참조", },
        ];

        // 전역 공통 data 입력
        gsCommonData = {
            approver_dataSource: approver_dataSource,
            approver_dataSource4Search: approver_dataSource4Search,
            receiver_dataSource: receiver_dataSource,
            receiver_dataSource4Search: receiver_dataSource4Search,
            _ddlData_ApproverType: _ddlData_ApproverType,
            _ddlData_ReceiverType: _ddlData_ReceiverType,
        };

        // Grid 데이터 초기화
        gridApprover_dataSource = new kendo.data.DataSource({
            data: [],
            schema: {
                model: {
                    id: "gridApprover",
                    fields: { type: { type: "string" }, name: { type: "string" }, },
                },
            },
        });

        gridReceiver_dataSource = new kendo.data.DataSource({
            data: [],
            schema: {
                model: {
                    id: "gridReceiver",
                    fields: { type: { type: "string" }, name: { type: "string" }, },
                },
            },
        });
    };

    // Form 및 Grid 처리
    const fnSetForm = () => {
        $("#title").kendoTextBox({
            placeholder: "제목",
        });

        $("#approver").kendoComboBox({
            dataTextField: "text", dataValueField: "value",
            dataSource: gsCommonData.approver_dataSource4Search,
            filter: "contains", suggest: true, index: 0,
        });

        $("#receiver").kendoComboBox({
            dataTextField: "text", dataValueField: "value",
            dataSource: gsCommonData.receiver_dataSource4Search,
            filter: "contains", suggest: true, index: 0,
        });

        $("#gridApprover").kendoGrid({
            dataSource: gridApprover_dataSource,
            height: "100%", toolbar: "<div></div>",
            // toolbar: "<button class='k-grid-custom-del  k-grid-삭제 k-button k-button-md k-rounded-md k-button-solid k-button-solid-base' onclick='delclickRow(\"gridApprover\")'>삭제</button>",
            columns: [
                { draggable: true, width: "40px" },
                {
                    field: "type", title: "유형", width: "100px",
                    template: columnDDLType("ApproverType"),
                    attributes: { style: "text-align: center;" },
                    model: { type: "string", editable: true },
                },
                {
                    field: "name", title: "성명", width: "200px",
                    template: columnDDLType("ApproverName"),
                    attributes: { style: "text-align: center;" },
                    model: { type: "string", editable: false },
                },
                {
                    command: [{text: "삭제",click: fnGridDel,},], width: "40px",
                },
            ],
            dataBound: function (e) {
                var grid = e.sender;
                var items = e.sender.items();

                items.each(function (e) {
                    var dataItem = grid.dataItem(this);

                    // 유형(type) ddl 설정
                    var ddtType = $(this).find(".ddlTemplateApproverType");

                    $(ddtType).kendoDropDownList({
                        value: dataItem.type,
                        dataSource: gsCommonData._ddlData_ApproverType,
                        dataTextField: "displayValue",
                        dataValueField: "value",
                        change: onDDLTypeChange,
                    });

                    // 성명(name) ddl 설정
                    var ddtName = $(this).find(".ddlTemplateApproverName");

                    $(ddtName).kendoDropDownList({
                        value: dataItem.name,
                        dataSource: gsCommonData.approver_dataSource,
                        dataTextField: "text",
                        dataValueField: "value",
                        change: onDDLTypeChange,
                    });
                });
            },
            navigatable: true,
            reorderable: {
                rows: true,
            },
            rowReorder: onRowRordered,
        });

        $("#gridReceiver").kendoGrid({
            dataSource: gridReceiver_dataSource,
            height: "100%", toolbar: "<div></div>",
            // toolbar: "<button class='k-grid-custom-del  k-grid-삭제 k-button k-button-md k-rounded-md k-button-solid k-button-solid-base' onclick='delclickRow(\"gridReceiver\")'>삭제</button>",
            columns: [
                { draggable: true, width: "40px" },
                {
                    field: "type", title: "유형", width: "100px",
                    template: columnDDLType("ReceiverType"),
                    attributes: { style: "text-align: center;" },
                    model: { type: "string", editable: true },
                    hidden: false,
                },
                {
                    field: "name", title: "성명", width: "200px",
                    template: columnDDLType("ReceiverName"),
                    attributes: { style: "text-align: center;" },
                    model: { type: "string", editable: false },
                    hidden: false,
                },
                {
                    command: [
                        {
                            text: "삭제", className: "k-grid-custom-row-del",
                            attributes: { style: "text-align: center;" },
                            click: fnGridDel,
                        },
                    ],
                    width: "40px",
                },
            ],
            dataBound: function (e) {
                var grid = e.sender;
                var items = e.sender.items();

                items.each(function (e) {
                    var dataItem = grid.dataItem(this);

                    // 유형(type) ddl 설정
                    var ddt = $(this).find(".ddlTemplateReceiverType");

                    $(ddt).kendoDropDownList({
                        value: dataItem.type,
                        dataSource: gsCommonData._ddlData_ReceiverType,
                        dataTextField: "displayValue",
                        dataValueField: "value",
                        change: onDDLTypeChange,
                    });

                    // 성명(name) ddl 설정
                    var ddtName = $(this).find(".ddlTemplateReceiverName");

                    $(ddtName).kendoDropDownList({
                        value: dataItem.name,
                        dataSource: gsCommonData.receiver_dataSource,
                        dataTextField: "text",
                        dataValueField: "value",
                        change: onDDLTypeChange,
                    });
                });
            },
            navigatable: true,
            reorderable: {
                rows: true,
            },
            rowReorder: onRowRordered,
        });

        $("#approvalcontent").kendoTextBox({
            label: "내용",
            placeholder: "내용",
        });

        // 전역 grid 변수 처리
        gridApprover = $("#gridApprover").data("kendoGrid");
        gridReceiver = $("#gridReceiver").data("kendoGrid");
    };

    // Grid Button 클릭 처리
    const fnSetButton = () => {
        var title = $("#title").data("kendoTextBox");
        var approver = $("#approver").data("kendoComboBox");
        var receiver = $("#receiver").data("kendoComboBox");

        $("#requestApproval").kendoButton({
            click: function (e) {
                if (gridApprover.dataSource._view.length == 0) {
                    alert("결재자가 없습니다. 결재자를 입력해주세요.");
                } else if (gridReceiver.dataSource._view.length == 0) {
                    alert("수신자가 없습니다. 수신자를 입력해주세요.");
                } else if ($("#title").val() == "") {
                    alert("제목이 비어있습니다. 제목을 입력해주세요.");
                } else {
                    requestApproval();
                }
            },
        });

        $("#requestApprovalTEST").kendoButton({
            click: function (e) {
                if ($("#title").val() == "") {
                    alert("제목이 비어있습니다. 제목을 입력해주세요.");
                } else {
                    // Form Click Event 실행
                    fnFormSubmitTEST();
                }
            },
        });
        $("#addApprover").kendoButton({
            click: function (e) {
                console.log("e:", e);
                console.log("addApprover - approver.value:" + approver.value());
                addRow("gridApprover", approver.value());
            },
        });

        $("#addReceiver").kendoButton({
            click: function (e) {
                console.log("e:", e);
                console.log("addReceiver - receiver.value:" + receiver.value());
                addRow("gridReceiver", receiver.value());
            },
        });
    };

    // 로그인 사용자 정보 조회
    const getMyProfile = async () => {
        $.ajax({
            type: "get",
            url: "/3dspace/resources/v1/modeler/gscprofiles/myprofile?$include=all&$fields=all&state=Active",
            dataType: "json",
            success: function (data) {
                console.log("getMyProfile - data:", data);
                myProfile.name = data.data[0].dataelements.name;
                myProfile.ENO_CSRF_TOKEN = data.csrf.value;
                myProfile.SecurityContext =
                    data.data[0].dataelements.defaultsecuritycontext;
                myProfile.deptcode = data.data[0].dataelements.deptcode;

                // 전역 변수 데이터 조회 체크용
                ChkGetData.myProfile = true;
            },
            error: function (request, status, error) {
                console.log("Ajax 사용자 정보 조회 Error - request,status,error:", request, status, error);

                alert("사용자 정보를 가져올 수 없습니다.\n관리자에게 문의바랍니다.");
            },
        });
    };

    // 유저 정보 조회
    const getPerson = async () => {
        $.ajax({
            type: "get",
            // url: "/sample/gsPerson.json", // 로컬 테스트용
            url: "/3dspace/resources/v1/modeler/gscprofiles?$include=none&$fields=none,name,fullname&state=Active",
            dataType: "json",
            success: function (data) {
                console.log("getPerson - data:", data);
                data_Person = data.data;

                data_Person.forEach((data, index) => {
                    // console.log("Index: ", index, " Value: ", data);
                    data_Person[index].value = data_Person[index].dataelements.name;
                    data_Person[index].text = data_Person[index].dataelements.fullname;
                });

                // 전역 변수 데이터 조회 체크용
                ChkGetData.person = true;
            },
            error: function (request, status, error) {
                console.log("Ajax 유저 정보 Error - request,status,error:", request, status, error);

                alert("유저 정보를 가져올 수 없습니다.\n관리자에게 문의바랍니다.");
            },
        });
    };

    // 부서 정보 조회
    const getBusinessUnit = async () => {
        $.ajax({
            type: "get",
            // url: "/sample/gsBusinessUnit.json", // 로컬 테스트용
            url: "/3dspace/resources/v1/modeler/gscbusinessunits?$include=none&$fields=none,name,title",
            dataType: "json",
            success: function (data) {
                console.log("getBusinessUnit - data:", data);
                data_BusinessUnit = data.data;

                data_BusinessUnit.forEach((data, index) => {
                    // console.log("Index: ", index, " Value: ", data);
                    data_BusinessUnit[index].value = data_BusinessUnit[index].dataelements.name;
                    data_BusinessUnit[index].text = data_BusinessUnit[index].dataelements.title;
                });

                // 전역 변수 데이터 조회 체크용
                ChkGetData.businessUnit = true;
            },
            error: function (request, status, error) {
                console.log("Ajax 부서 정보 조회 Error - request,status,error:", request, status, error);

                alert("부서 정보를 가져올 수 없습니다.\n관리자에게 문의바랍니다.");
            },
        });
    };

    // 필수값 체크
    const fnChkValue = () => {
        // 프로젝트 ID 체크
        if (projectInfo.projectId == undefined || projectInfo.projectId == "") {
            if (confirm("프로젝트 ID가 없습니다. 확인을 누르시면 창이 닫힙니다.")) {
                window.close();
            }
        } else {
            fnSetProject();
        }
    };

    // 조건에 따른 기본값 설정
    const fnSetGridData = () => {
        
        const intervalSetGridData = setInterval(function(){
            if(!ChkSetGridData && ChkCount < 5){
                console.log("ChkGetData:", ChkGetData, "ChkSetGridData:", ChkSetGridData, "ChkCount:", ChkCount);

                // myProfile, person, businessUnit 정보를 조회한 경우, 실행
                if(ChkGetData.myProfile && ChkGetData.person && ChkGetData.businessUnit){
                // if(ChkGetData.person && ChkGetData.businessUnit){ // 로컬 테스트용

                    // Combobox 데이터 재설정 (검색 시, DataSource 변경에 따른 별도 데이터 처리)
                    gsCommonData.approver_dataSource = new kendo.data.DataSource({
                        data: data_Person,
                    });
                    gsCommonData.approver_dataSource4Search = new kendo.data.DataSource({
                        data: data_Person,
                    });

                    // 검색 및 추가 데이터 설정
                    var kendoComboBox = $("#approver").data("kendoComboBox");
                    kendoComboBox.setDataSource(gsCommonData.approver_dataSource4Search);

                    // Combobox 데이터 재설정 (검색 시, DataSource 변경에 따른 별도 데이터 처리)
                    gsCommonData.receiver_dataSource = new kendo.data.DataSource({
                        data: data_BusinessUnit.concat(data_Person),
                    });
                    gsCommonData.receiver_dataSource4Search = new kendo.data.DataSource({
                        data: data_BusinessUnit.concat(data_Person),
                    });

                    // 검색 및 추가 데이터 설정
                    var kendoComboBox = $("#receiver").data("kendoComboBox");
                    kendoComboBox.setDataSource(gsCommonData.receiver_dataSource4Search);

                    // 결재 종류별 결재자 Grid 자동 설정
                    var approvalType = urlParams.get("approvalType")
                    // 과제계획승인 인 경우,
                    if(approvalType === "PrjPlanApproval"){
                        gridApprover_dataSource = new kendo.data.DataSource({
                            data: [
                                {
                                    type: "A", // 결재
                                    name: "C13869", // 조정희책임
                                },
                                {
                                    type: "C", // 합의
                                    name: "C16890", // 이경준
                                },
                                {
                                    type: "Y", // 확인
                                    name: "C18044", // 지윤주팀장
                                },
                            ],
                            schema: { model: { id: "gridApprover", fields: { type: { type: "string" }, name: { type: "string" }, }, }, },
                        });

                        gridReceiver_dataSource = new kendo.data.DataSource({
                            data: [
                                {
                                    type: "V",
                                    // name: "C16031",
                                    name: myProfile.name, // 로그인 사용자
                                },
                                {
                                    type: "V",
                                    // name: "R58000",
                                    name: myProfile.deptcode, // 로그인 사용자 부서
                                },
                            ],
                            schema: { model: { id: "gridReceiver", fields: { type: { type: "string" }, name: { type: "string" }, }, }, },
                        });
                    // 과제종료승인 인 경우,
                    }else if(approvalType === "PrjPlanApproval"){
                        gridApprover_dataSource = new kendo.data.DataSource({
                            data: [
                                {
                                    type: "A", // 결재
                                    name: "C13869", // 조정희책임
                                },
                                {
                                    type: "C", // 합의
                                    name: "C16890", // 이경준
                                },
                                {
                                    type: "Y", // 확인
                                    name: "C18044", // 지윤주팀장
                                },
                            ],
                            schema: { model: { id: "gridApprover", fields: { type: { type: "string" }, name: { type: "string" }, }, }, },
                        });

                        gridReceiver_dataSource = new kendo.data.DataSource({
                            data: [
                                {
                                    type: "V",
                                    // name: "C16031",
                                    name: myProfile.name, // 로그인 사용자
                                },
                                {
                                    type: "V",
                                    // name: "R58000",
                                    name: myProfile.deptcode, // 로그인 사용자 부서
                                },
                            ],
                            schema: { model: { id: "gridReceiver", fields: { type: { type: "string" }, name: { type: "string" }, }, }, },
                        });
                    }else{
                        gridApprover_dataSource = new kendo.data.DataSource({
                            data: [
                                {
                                    type: "A", // 결재
                                    name: "C13869", // 조정희책임
                                },
                                {
                                    type: "C", // 합의
                                    name: "C16890", // 이경준
                                },
                                {
                                    type: "Y", // 확인
                                    name: "C18044", // 지윤주팀장
                                },
                            ],
                            schema: { model: { id: "gridApprover", fields: { type: { type: "string" }, name: { type: "string" }, }, }, },
                        });

                        gridReceiver_dataSource = new kendo.data.DataSource({
                            data: [
                                {
                                    type: "V",
                                    // name: "C16031",
                                    name: myProfile.name, // 로그인 사용자
                                },
                                {
                                    type: "V",
                                    // name: "R58000",
                                    name: myProfile.deptcode, // 로그인 사용자 부서
                                },
                            ],
                            schema: { model: { id: "gridReceiver", fields: { type: { type: "string" }, name: { type: "string" }, }, }, },
                        });
                    }
                    
                    // grid dataSource 데이터 입력 Sync
                    gridApprover.setDataSource(gridApprover_dataSource);
                    gridApprover.dataSource.sync();        

                    // grid dataSource 데이터 입력 Sync
                    gridReceiver.setDataSource(gridReceiver_dataSource);
                    gridReceiver.dataSource.sync();

                    // 전역 SetGridData 확인용
                    ChkSetGridData = true;

                    console.log("gridApprover:",gridApprover);
                    console.log("gridReceiver:",gridReceiver);                    
                }

                // setInterval 반복 횟수 체크용
                ChkCount ++;

            }else if(ChkCount == 5){
                alert("데이터 조회 및 초기화 중 오류가 발생하였습니다.\n관리자에게 문의하세요.");
                console.log("fnSetGridData Failed");
                clearInterval(intervalSetGridData);

            }else if(ChkSetGridData){
                console.log("fnSetGridData Completed");
                clearInterval(intervalSetGridData);
            }
        }, 1000);
    };

    

    // define function
    function onRowRordered(ev) {
        // console.log("onRowRordered - ev:", ev);

        var grid = ev.sender,
            dataSource = grid.dataSource,
            externalGrid,
            externalDataItem;

        // console.log("ev.oldIndex:", ev.oldIndex);

        if (ev.oldIndex !== -1) {
            // Row dropped from external grid
            ev.preventDefault();
            externalGrid = ev.row.parents(".k-grid").data("kendoGrid");

            externalDataItem = externalGrid.dataItem(ev.row);
            externalDataItem.Discontinued === true
                ? (externalDataItem.Discontinued = false)
                : (externalDataItem.Discontinued = true);

            externalGrid.dataSource.remove(externalDataItem);
            dataSource.insert(ev.newIndex, externalDataItem.toJSON());
        }
    }

    function columnDDLType(gridType) {
        var input = '<input class="ddlTemplate' + gridType + '"/>';

        return input;
    }

    function onDDLTypeChange(e) {
        var gridType = "";
        var DDLType = "";
        var element = e.sender.element;
        var row = element.closest("tr");
        var gridClass = element[0].className.split("ddlTemplate")[1];

        // Classs 명에서 column 별 설정
        if (gridClass.includes("Type")) {
            DDLType = "type";
            gridType = gridClass.split("Type")[0];
        } else if (gridClass.includes("Name")) {
            DDLType = "name";
            gridType = gridClass.split("Name")[0];
        }

        var grid = $("#grid" + gridType).data("kendoGrid");
        var dataItem = grid.dataItem(row);

        // DDLType 별로 value 세팅
        dataItem.set(DDLType, e.sender.value());
    }

    // 행추가 - gridId 에 따라 해당 data 생성
    function addRow(gridId, data) {
        console.log("addRow - gridId:", gridId);
        console.log("addRow - data:", data);

        if (data != "") {
            var grid = $("#" + gridId).data("kendoGrid");
            var dataSource = grid.dataSource;
            var dataSource_length = dataSource.data().length;
            var gridDefaultType =
                gridId == "gridApprover" ? "A" : gridId == "gridReceiver" ? "V" : ""; // type 값 입력: 결재자 - 결재, 수신/참조 - 수신
            var input_data = { type: gridDefaultType, name: data };

            dataSource.insert(dataSource_length, input_data);
        } else {
            alert("값이 비어있습니다. 값 선택 후, 버튼을 클릭해주세요.");
        }

        // grid.add(grid.select());
    }

    // 툴바에서 체크한 행삭제 기능
    function delclickRow(gridId) {
        var grid = $("#" + gridId).data("kendoGrid");

        grid.removeRow(grid.select());
    }

    // 행에서 해당 행삭제 기능
    function fnGridDel(e) {
        var tr = $(e.target).closest("tr");
        var gridId = $(tr).closest("[data-role='grid']").attr("id");
        var grid = $("#" + gridId).data("kendoGrid");

        grid.removeRow(tr);
    }

    function requestApproval() {
        console.log("requestApproval...");

        // Route 선언
        // var today = new Date();
        // var Route_title = today.getFullYear() +  "_" + (today.getMonth() + 1) + "_" + today.getDate() + "_" + today.getHours() + today.getMinutes();

        var Route_title = $("#title").val();
        var Route_approvalLine = {};
        var Route_approver = [];
        var Route_receiver = [];
        var Route_contents =
            "<html><h1>결재 요청</h1><p>" + $("#approvalcontent").val() + "</p></html>";
        var Route_objectId = projectInfo.projectId;
        // var Route_objectId = "29A105550000470464379DDB0001644A";

        // TOAS FormData 선언
        var TOAS_formData_v02 = "";

        gridApprover.dataSource._view.forEach((element) => {
            var approver_data = {};
            approver_data["approveType"] = element.type;
            approver_data["approver"] = element.name;
            Route_approver.push(approver_data);

            if (element.type == "X" || element.type == "Y")
                TOAS_formData_v02 =
                    TOAS_formData_v02 + element.type + "^C1^" + element.name + "^N^P;";
            else
                TOAS_formData_v02 =
                    TOAS_formData_v02 + element.type + "^C1^" + element.name + "^Y^P;";
        });

        gridReceiver.dataSource._view.forEach((element) => {
            var receiver_data = {};
            receiver_data["approveType"] = element.type;
            receiver_data["approver"] = element.name;
            Route_receiver.push(receiver_data);

            if (!element.name.includes("C"))
                TOAS_formData_v02 =
                    TOAS_formData_v02 + element.type + "^C1^" + element.name + "^Y^D;";
            else
                TOAS_formData_v02 =
                    TOAS_formData_v02 + element.type + "^C1^" + element.name + "^Y^P;";
        });

        var Route_inputData = {
            title: Route_title,
            approvalLine: {
                approver: Route_approver,
                receiver: Route_receiver,
            },
            contents: Route_contents,
            objectId: Route_objectId,
        };

        // 마지막 콤마 삭제
        TOAS_formData_v02 = TOAS_formData_v02.substring(
            0,
            TOAS_formData_v02.length - 1
        );

        console.log("Route_inputData:", Route_inputData);
        console.log(
            "JSON.stringify(Route_inputData):",
            JSON.stringify(Route_inputData)
        );
        console.log("TOAS_formData_v02:", TOAS_formData_v02);

        // Route 생성 API
        $.ajax({
            type: "POST",
            url: "/3dspace/resources/v1/modeler/gscRoute/approval",
            headers: {
                ENO_CSRF_TOKEN: myProfile.ENO_CSRF_TOKEN,
                SecurityContext: myProfile.SecurityContext,
            },
            data: JSON.stringify(Route_inputData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend: function (xhr) {
                WAIT.startLoader();
            },
            success: function (data) {
                console.log(data);
                var Route_id = data.data[0].id;
                console.log("Route 생성 완료 Route ID:", Route_id);

                // form value 설정
                document.getElementsByName("create_emp_id")[0].value = myProfile.name;
                document.getElementsByName("v01")[0].value = Route_contents;
                document.getElementsByName("v02")[0].value = TOAS_formData_v02;
                document.getElementsByName("v03")[0].value = Route_id;
                document.getElementsByName("v04")[0].value = Route_title;

                fnFormSubmit();
            },
            error: function (request, status, error) {
                console.log("Ajax Route 생성 Error - request,status,error:", request, status, error);

                alert("결재 요청시, 오류가 발생했습니다.\n관리자에게 문의하십시오.");
            },
            complete: function (response, status) {
                console.log("complete - response, status:", response, status);
                WAIT.stopLoader();
            },
        });
    }

    function fnFormSubmit() {
        // Form POST Button Click
        document.getElementById("requestApprovalFormSubmitButton").click();

        // gscApprovalRequest.jsp 창 닫기
        window.close();
    }

    function fnFormSubmitTEST() {
        // form value 설정 - TEST 데이터
        document.getElementsByName("create_emp_id")[0].value = "C16890";
        document.getElementsByName("v01")[0].value =
            "<html><h1>결재 요청 API 테스트 230418</h1> <p>결재 요청 API 테스트</p></html>";
        document.getElementsByName("v02")[0].value =
            "A^C1^C13869^Y^P;C^C1^C16031^Y^P;Y^C1^C18044^Y^P;V^C1^C16890^Y^P;V^C1^R58000^Y^D";
        document.getElementsByName("v03")[0].value =
            "56A0C1CA00004BCC642FCD6200001732";
        document.getElementsByName("v04")[0].value = "결재 요청 API 테스트";

        // Form POST Button Click
        document.getElementById("requestApprovalFormSubmitButton").click();
    }

    function fnSubmit(form) {
        var form = document.getElementById("requestApprovalForm");

        try {
            var targetWin = window.open(
                "about:blank",
                "targetWin",
                "top=0,left=0,width=870,height=700,resizable=1"
            );
            form.action = toasURL;
            form.target = "targetWin";
            form.focus();

            return true;
        } catch (e) {
            if (typeof e == "object") alert(e.number + "\n" + e.description);
            else alert(e);

            return false;
        }
    }
</script>
