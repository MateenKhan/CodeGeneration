<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Code Generation</title>
<link rel="stylesheet"
	href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/foundation/6.4.3/css/foundation.min.css">
<style>
.hidden, #code {
	display: none;
}

.file{
	    display: inline;
}
.java-files-table, .angular-files-table{
	margin-left: 20px;
}

.angular-files-table-div{
	padding-left: 20px;
}

body {
  padding: 30px;
}

.demo {
  background: #1779ba;
}

.header-label{
	color: black;
    font-weight: bold;
}

.cell {

  line-height: 50px;
  height: 50px;
  color: white;
  text-align: center;
  margin-bottom: 30px;
}


</style>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/foundation/6.4.3/js/foundation.min.js"></script>
<script>
	$(function() {
		var availableTags =<%=application.getAttribute("tables")%>;
		$("#tags").autocomplete({
			source : availableTags,
			select : function(a, b) {
				$("#result").text($("#database").val()+" : "+b.item.value);
				$("#table").text(b.item.value);
				$("#code").show(200);
				$("#pk_div").show(200);
			}
		});
	});
	function downloadCode() {

		var table = $("#table").text();
		var pk = $("#pk").val();
		var database = $("#database").val();
		var pojo = $("#pojo").is(':checked');
		var dao = $("#dao").is(':checked');
		var daoImpl = $("#daoImpl").is(':checked');
		var querys = $("#querys").is(':checked');
		var controller = $("#controller").is(':checked');
		var controllerImpl = $("#controllerImpl").is(':checked');
		var pk = $("#pk").val();
		url = window.location.protocol +"//"+window.location.host+""+window.location.pathname;
		url += "code?table="+table+"&pk="+pk+"&database="+database+"&pojo="+pojo+"&dao="+dao+"&daoImpl="+daoImpl;
		url += "&querys="+querys+"&controller="+controller+"&controllerImpl="+controllerImpl;
		window.open(url);
		/*let link= jQuery('<a></a>');
		link[0].href=url;
		link[0].click();
		var filesJson = {};
		filesJson.table = table;
		filesJson.pk = pk;
		filesJson.database = database;
		filesJson.pojo = pojo;
		filesJson.dao = dao;
		filesJson.daoImpl = daoImpl;
		filesJson.querys = querys;
		filesJson.controller = controller;
		filesJson.controllerImpl = controllerImpl;
		$.ajax({
			type : "GET",
			url : window.location.protocol + "//" + window.location.host + ""
					+ window.location.pathname + "code",
			//contentType : "application/json",
			data : JSON.stringify(filesJson),
			success : function(response, status, xhr) {
				var blob=new Blob([response], {type:"application/zip"});
                let link= jQuery('<a></a>');
                link[0].href= URL.createObjectURL(blob);
                link[0].download= "asdf.zip";
                link[0].click();
			},
			error : function(data) {
				showError(data)
			}
		});*/
	}

	
	function showError(text){
		alert(text);
	}
	
	function loadNewTables() {
		$.ajax({
			type : "POST",
			url : window.location.protocol + "//" + window.location.host + ""
					+ window.location.pathname + "load",
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				if (data) {
					$("#tags").autocomplete('option', 'source',
							JSON.parse(data));
				}
			},
			error : function(data) {
				showError(data)
			}
		});
	}
</script>
</head>
<body>
	<div class="grid-x">
		<div class="small-10 cell">
				<h1 class="header-label" id="result"> Auto Code Generate </h1>
		</div>
	</div>

	<div class="grid-x">
		<div class="small-4 cell">
			<table>
				<tr>
					<td>
						<div class="ui-widget">
							<label for="tags">Table: </label> 
						</div>
					</td>
					<td>
						<input id="tags">
						<input id="table" class="hidden">
					</td>
				</tr>
				<tr>
					<td><label>Database: </label></td><td><input id="database" type="text" value="qount" readonly></td>
				</tr>
				<tr>
					<td><label id="pk_div">pk: </label></td><td><input type="text" id="pk" value="id" readonly/></td>
				</tr>
			</table>
			<button class="button" id="loadNewTables" onclick="loadNewTables()">Load New Tables</button>
			<button class="button" id="code" onclick="downloadCode()">Generate Code</button>
			<a href="" class="hidden" id="codeDownload"></a>
		</div>
	
		<div class="small-4 cell">
			<table class="java-files-table">
				<tr><tr><th><label class='file-header'>Java Files:</label></th><th><input id="java-check" type="checkbox" checked></th></tr>
				<tr>
					<td><label class='file'>Pojo:</label></td><td><input id="pojo" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>Dao:</label></td><td><input id="dao" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>DaoImpl:</label></td><td><input id="daoImpl" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>Querys:</label></td><td><input id="querys" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>Controller:</label></td><td><input id="controller" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>ControllerImpl:</label></td><td><input id="controllerImpl" type="checkbox"
						checked>
					</td>
				</tr>
			</table>
		</div>
		<div class="small-4 cell angular-files-table-div">
			<table class="angular-files-table">
				<tr><tr><th><label class='file-header'>Angular Files:</label></th><th><input id="angular-check" type="checkbox" checked></th></tr>
				<tr>
					<td><label class='file'>Component:</label></td><td><input id="Component" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>Form:</label></td><td><input id="Form" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>Model:</label></td><td><input id="Model" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>Service:</label></td><td><input id="Service" type="checkbox" checked></td>
				</tr>
				<tr>
					<td><label class='file'>Html:</label></td><td><input id="Html" type="checkbox" checked></td>
				</tr>
			</table>
		</div>
		
	</div>

</body>
</html>