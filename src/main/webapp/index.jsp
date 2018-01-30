<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>jQuery UI Autocomplete - Default functionality</title>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<style>
.hidden, #code {
	display: none;
}
</style>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		var availableTags =<%=application.getAttribute("tables")%>;
		$("#tags").autocomplete({
			source : availableTags,
			select : function(a, b) {
				$("#result").text(b.item.value);
				$("#code").show(200);
				$("#pk_div").show(200);
			}
		});
	});
	function downloadCode() {

		var table = $("#result").text();
		var pk = $("#pk").val();
		var database = $("#database").val();
		var pojo = $("#pojo").is(':checked');
		var dao = $("#dao").is(':checked');
		var daoImpl = $("#daoImpl").is(':checked');
		var querys = $("#querys").is(':checked');
		var controller = $("#controller").is(':checked');
		var controllerImpl = $("#controllerImpl").is(':checked');
		var table = $("#result").text();
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

	<button id="loadNewTables" onclick="loadNewTables()">Load New
		Tables</button>
	<table>
		<tr>
			<td><h1 id="result"></h1></td>
		</tr>
		<tr>
			<td>
				<div class="ui-widget">
					<label for="tags">Tags: </label> <input id="tags">
				</div>
			</td>
		</tr>
		<tr>
			<td><input id="database" type="text" value="qount"></td>
		</tr>
		<tr>
			<td>Pojo:<input id="pojo" type="checkbox" checked></td>
		</tr>
		<tr>
			<td>Dao:<input id="dao" type="checkbox" checked></td>
		</tr>
		<tr>
			<td>DaoImpl:<input id="daoImpl" type="checkbox" checked></td>
		</tr>
		<tr>
			<td>Querys:<input id="querys" type="checkbox" checked></td>
		</tr>
		<tr>
			<td>Controller:<input id="controller" type="checkbox" checked></td>
		</tr>

		<tr>
			<td>ControllerImpl:<input id="controllerImpl" type="checkbox"
				checked>
			</td>
		</tr>

	</table>
	<button id="code" onclick="downloadCode()">Generate Code</button>
	<a href="" class="hidden" id="codeDownload"></a>
	<br />
	<div class="hidden" id="pk_div">
		pk :<input type="text" id="pk" value="id" />
	</div>
</body>
</html>