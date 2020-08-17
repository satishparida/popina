function loadMenu(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var menu = JSON.parse(this.responseText);
		    
		    var html = '<form>';
		    for (item in menu) {
		    	html += menu[item].productName + ' /Cost: ' + menu[item].productCost 
		    			+ ' /Availevel: ' + menu[item].productAvalibility 
		    			+ ' /Order Quantity: <input type="text" name="item' + menu[item].productId 
		    			+ ' onkeypress = "return onlyNumberKey(event)" /> <br>';
		    }
		    
		    html += '<input type="button" value="Place Order" onclick="placeOrder()"/>';
		    html += '<input type="button" value="Reset" onclick="resetInputFields()"/>';
		    html += '</form>';
		    
		    var content = document.getElementById("menu_div");
		    content.innerHTML = html;
		    
		    html = '<br><a href="logout">logout</a>';
		    content = document.getElementById("logout");
		    content.innerHTML = html;
		}
		else if (this.status == 404){
			var html = '<h2>404: Page Not Found.</h2><br> Try Login <a href="login.html">Here</a>';
			var content = document.getElementById("menu_div");
		    content.innerHTML = html;
		}
	}
	request.open("GET","menu",true);
	request.send();
}


function placeOrder(){
	var order = [];
	var itemCount = document.querySelectorAll('#menu_div input[type="text"]').length;
	
	for (i = 0; i < itemCount; i++) {
		var quantity = document.querySelectorAll('#menu_div input[type="text"]')[i].value;
		var itemid = document.querySelectorAll('#menu_div input[type="text"]')[i].name.substr(4);
		if (quantity != "") {
			order.push({itemid,quantity});
		}
	}
	var orderJSON = JSON.stringify(order);
	
	var request = new XMLHttpRequest();
	
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var orderid = this.responseText;
			var content = document.getElementById("order_status");
			if (orderid == 0){
				var html = '<br>Order Could not be placed, Sorry!<br>';
				html += '<a href="menu.html">New Order</a>';
				content.innerHTML = html;
			}
			else {
				var html = '<br>Order Placed with Order id: '+ orderid +'<br>';
				html += '<a href="menu.html">New Order</a>';
				content.innerHTML = html;
			}
		}
	}
	request.open("POST","placeOrder");
	request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	request.send(orderJSON);
}


function resetInputFields(){
	var itemCount = document.querySelectorAll('#menu_div input[type="text"]').length;
	
	for (i = 0; i < itemCount; i++) {
		document.querySelectorAll('#menu_div input[type="text"]')[i].value="";
	}
}

function onlyNumberKey(evt) { 
    // Only ASCII charactar in that range allowed 
    var ASCIICode = (evt.which) ? evt.which : evt.keyCode 
    if (ASCIICode > 31 && ASCIICode < 39 )
        return false; 
    return true; 
}
