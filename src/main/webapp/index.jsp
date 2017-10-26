<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>jQuery UI Autocomplete - Default functionality</title>
  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <style>
  	.hidden,#code{
  		display: none;
  	}
  	{
  		display: none;
  	}
  </style>
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script>
  $( function() {
    var availableTags = <%=application.getAttribute("tables") %>;
    $( "#tags" ).autocomplete({
      source: availableTags,
      select: function (a, b) {
          $("#result").text(b.item.value);
          $("#code").show(200);
          $("#pk_div").show(200);
      }
    });
  } );
  function downloadCode(){
	  var table = $("#result").text();
	  var pk = $("#pk").val();
	  url = window.location.protocol +"//"+window.location.host+""+window.location.pathname;
	  url += "code?table="+table+"&pk="+pk;
	  window.location.href = url;
  }
  
  function loadNewTables(){
	  $.ajax({
			type : "POST",
			url : window.location.protocol +"//"+window.location.host+""+window.location.pathname+"load",
			contentType : "application/json; charset=utf-8",
			success : function(data) {
				if(data){
		            $( "#tags" ).autocomplete('option', 'source', JSON.parse(data));
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
 
<button id="loadNewTables" onclick="loadNewTables()">Load New Tables</button>
 <h1 id="result"></h1>
<div class="ui-widget">
  <label for="tags">Tags: </label>
  <input id="tags">
</div>
<button id="code" onclick="downloadCode()">Generate Code</button>
 <a href="" class="hidden" id="codeDownload"></a>
 <br/>
 <div class ="hidden" id="pk_div">
 	pk :<input type="text" id="pk" value="id"/>
 </div>
</body>
</html>