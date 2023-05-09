<#ftl encoding="utf-8">
<!DOCTYPE html>
<html>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<style>
    body {
        margin: 0px;
    }

    td {
        font-size: 12px;
        font-family: "돋움";
        color: #444444;
        line-height: 16px;
    }

    .M_table_line {
        width: 100%;
        border-collapse: collapse;
        border-color: #c0c0c0;
        border-style: solid;
        border-width: 1px;
    }

    .M_table_title_c {
        border-style: solid;
        border-width: 1px;
        line-height: 18px;
        background-color: #f0f0f0;
        color: #444444;
        text-align: center;
        height: 22px;
        font-weight: bold;
    }

    .table_con_l {
        border-style: solid;
        border-width: 1px;
        line-height: 18px;
        text-align: left;
        padding-left: 5px;
        padding-right: 5px;
        height: 22px;
    }

    .table_con_c {
        border-style: solid;
        border-width: 1px;
        line-height: 18px;
        text-align: center;
        height: 22px;
    }
</style>
<head>
    <title>${title}</title>
</head>
<body>
<h1>${prj_name}</h1>
<table border="1" cellpadding="0" cellspacing="0" class="M_table_line">
    <tr>
        <td class="M_table_title_c" style="width:15%">과제 ID</td>
        <td class="table_con_l" style="width:85%">${prj_id}</td>
    </tr>

    <tr>
        <td class="M_table_title_c" style="width:15%">과제 이름</td>
        <td class="table_con_l" style="width:85%">${prj_name}</td>
    </tr>
    <tr>
        <td class="M_table_title_c" style="width:15%">과제 생성일</td>
        <td class="table_con_l" style="width:85%">${prj_originated}</td>
    </tr>
    <tr>
        <td class="M_table_title_c" style="width:15%">과제 시작일</td>
        <td class="table_con_l" style="width:85%">${prj_startDate}</td>
    </tr>
    <tr>
        <td class="M_table_title_c" style="width:15%">과제 종료일</td>
        <td class="table_con_l" style="width:85%">${prj_endDate}</td>
    </tr>
    <tr>
        <td class="M_table_title_c" style="width:15%">과제 멤버</td>
        <td class="table_con_l" style="width:85%">${prj_member}</td>
    </tr>
    <tr>
        <td class="M_table_title_c" style="width:15%">과제 상태</td>
        <td class="table_con_l" style="width:85%">${prj_current}</td>
    </tr>
    <tr>
        <td class="M_table_title_c" style="width:15%">결재자</td>
        <td class="table_con_l" style="width:85%">
            <select name="pets" id="pet-select">
                <#list peopleList as person>
                    <option value="">--Please choose an option--</option>
                    <option value="dog">${person}</option>
                </#list>

            </select>
        </td>
    </tr>
    <tr>
        <td class="M_table_title_c" style="width:15%">합의자</td>
        <td class="table_con_l" style="width:85%"></td>
    </tr>
</table>
</body>
</html>
