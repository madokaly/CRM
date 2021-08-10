<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


<script type="text/javascript">

	$(function(){
		pageList(1,2);
		$("#addBtn").click(function () {

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			$.ajax({
				url : "workbench/activity/getUserList.do",
				type : "get",
				dataType : "json",
				success : function (data) {
					$("#create-marketActivityOwner").empty();
					$.each(data,function (i,n) {
						var str = "<option value='"+n.id+"'>"+n.name+"</option>";
						$("#create-marketActivityOwner").append(str);
					})
					var id = "${sessionScope.user.id}";
					$("#create-marketActivityOwner").val(id);
					$("#createActivityModal").modal("show");/*hide隐藏*/
				}
			})
		})

		$("#saveBtn").click(function () {
			$.ajax({
				url : "workbench/activity/save.do",
				type : "post",
				data : {
					"owner" : $.trim($("#create-marketActivityOwner").val()),
					"name" : $.trim($("#create-marketActivityName").val()),
					"startDate" : $.trim($("#create-startTime").val()),
					"endDate" : $.trim($("#create-endTime").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-describe").val())
				},
				dataType : "json",
				success : function (data) {
					if(data.success){

						pageList(1
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						$("#create-form")[0].reset();

						$("#createActivityModal").modal("hide");
					}else{
						alert("添加市场活动失败");
					}
				}
			})
		})


		$("#searchBtn").click(function () {
			$("#hidden-name").val( $.trim( $("#search-name").val() ) );
			$("#hidden-owner").val( $.trim( $("#search-owner").val() ) );
			$("#hidden-startDate").val( $.trim( $("#search-startDate").val() ) );
			$("#hidden-endDate").val( $.trim( $("#search-endDate").val() ) );

			pageList(1
					,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


		})
		$("#qx").click(function () {
			$("input[name=px]").prop("checked",this.checked);
		})
		$("#activityBody").on("click",$("input[name=px]"),function () {
			$("#qx").prop("checked",$("input[name=px]").length == $("input[name=px]:checked").length)
		})

        $("#deleteBtn").click(function () {

            var $px = $("input[name=px]:checked");
            var param = "";
            if($px.length == 0) {
                alert("请选择要删除的记录");
            }else{

            	if(confirm("确定删除所选记录吗？")){
					$.each($px,function (i,n) {
						param += "id="+n.value;
						if( i < $px.length-1 ) {
							param += "&";
						}
					})
					$.ajax({
						url : "workbench/activity/delete.do",
						type : "post",
						data : param,
						dataType : "json",
						success : function (data) {
							if(data.success){
								pageList(1
										,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							}else{
								alert("删除记录失败");
							}
						}
					})
				}
            }
        })
		
		$("#editBtn").click(function () {
			var $px = $("input[name=px]:checked");
			if ($px.length != 1) {
				alert("请选择1条要修改的记录");
			} else {
				var id = $px.val();
				$.ajax({
					url: "workbench/activity/getUserListAndActivity.do",
					type: "get",
					data: {
						"id": id
					},
					dataType: "json",
					success: function (data) {
						$("#edit-marketActivityOwner").empty();
						var str = "";
						$.each(data.uList,function (i,n) {
							str += "<option value='"+n.id+"'";
							if(n.id == data.a.owner){
								str += "selected >"+n.name+"</option>";
							}else{
								str += ">"+n.name+"</option>";
							}
						})
						$("#edit-marketActivityOwner").append(str);
						$("#edit-id").val(data.a.id);
						$("#edit-marketActivityName").val(data.a.name);
						$("#edit-startTime").val(data.a.startDate);
						$("#edit-endTime").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-describe").val(data.a.description);


						$("#editActivityModal").modal("show");
					}
				})
			}
		})
		$("#updateBtn").click(function () {
			$.ajax({
				url : "workbench/activity/update.do",
				type : "post",
				data : {
					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-marketActivityOwner").val()),
					"name" : $.trim($("#edit-marketActivityName").val()),
					"startDate" : $.trim($("#edit-startTime").val()),
					"endDate" : $.trim($("#edit-endTime").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-describe").val())
				},
				dataType : "json",
				success : function (data) {
					if(data.success){
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						$("#editActivityModal").modal("hide");
					}else{
						alert("修改市场活动失败");
					}
				}
			})
		})

	});


	function pageList(pageNo,pageSize) {

		$("#search-name").val( $.trim( $("#hidden-name").val() ) );
		$("#search-owner").val( $.trim( $("#hidden-owner").val() ) );
		$("#search-startDate").val( $.trim( $("#hidden-startDate").val() ) );
		$("#search-endDate").val( $.trim( $("#hidden-endDate").val() ) );

		$("#qx").prop("checked",false);

		$.ajax({
			url : "workbench/activity/pageList.do",
			type : "get",
			data : {
				"pageNo" : pageNo,
				"pageSize" : pageSize,
                "name" : $.trim( $("#search-name").val() ),
                "owner" : $.trim( $("#search-owner").val() ),
                "startDate" : $.trim( $("#search-startDate").val() ),
                "endDate" : $.trim( $("#search-endDate").val() ),
			},
			dataType : "json",
			success : function (data) {
                $("#activityBody").empty();
                $.each(data.dataList,function (i,n) {
                    var str = "";
                    str += "<tr class='active'>";
                    str += "<td><input type='checkbox' name='px' value='"+n.id+"'/></td>";
                    str += "<td><a style='text-decoration: none; cursor: pointer;' onclick=\"window.location.href='workbench/activity/detail.do?id="+n.id+"';\">"+n.name+"</a></td>";
                    str += "<td>"+n.owner+"</td>";
                    str += "<td>"+n.startDate+"</td>";
                    str += "<td>"+n.endDate+"</td>";
                    str += "</tr>";

                    $("#activityBody").append(str);
                    var totalPages = Math.ceil(data.total / pageSize);

					$("#activityPage").bs_pagination({
						currentPage: pageNo, // 页码
						rowsPerPage: pageSize, // 每页显示的记录条数
						maxRowsPerPage: 20, // 每页最多显示的记录条数
						totalPages: totalPages, // 总页数
						totalRows: data.total, // 总记录条数

						visiblePageLinks: 3, // 显示几个卡片

						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,

						onChangePage : function(event, data){
							pageList(data.currentPage , data.rowsPerPage);
						}
					});

				})
			}
		})
	}
	
</script>
</head>
<body>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="create-form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
	<%--					<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>