function loadAllPendingOrders(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var orders = JSON.parse(this.responseText);
			
			var html = '';
		    
			for (order in orders) {
				
				html += '<div id="div' + orders[order].orderid + '">';
				html += '<form id="form' + orders[order].orderid + '">';
				html += '<h3>Order Id: ' + orders[order].orderid + '</h3>';
				html += '<div id="innerDiv' + orders[order].orderid 
						+ '"><B>Current Status: </B>'+ orders[order].orderstatus.toUpperCase() + '</div><br>';
				html += '<input type="hidden" name="id" value="' + orders[order].orderid + '" />';
				
				for (item in orders[order].items){
					productName=orders[order].items[item].productname;
					quantity=orders[order].items[item].quantity;
					html += '<B>Name: </B>' + productName + ' /<B>Quantity: </B>' + quantity + '<br>';
				}
				
				html += '<br><select name="status">';
				html += '<option disabled selected value> -- update status -- </option>';
				html += '<option value="order placed">order placed</option>';
				html += '<option value="being prepared">being prepared</option>';
				html += '<option value="ready for collection">ready for collection</option>';
				html += '<option value="collected">collected</option>';
				
				html += '</select> <br>';
				html += '<input type="button" value="Update" onClick="updateOrderStatus(' + orders[order].orderid + ');">';
				html += '</form> </div><br>';
				
			}
			var content = document.getElementById("all_orders");
		    content.innerHTML = html;
		    
		    html = '<a href="logout">logout</a><br>';
		    content = document.getElementById("logout");
		    content.innerHTML = html;
		}
		else if (this.status == 404){
			var html = '<h2>404: Page Not Found.</h2><br> Try Login <a href="login.html">Here</a>';
			var content = document.getElementById("all_orders");
		    content.innerHTML = html;
		}
	}
	request.open("GET","kitchenmanager",true);
	request.send();
}

function updateOrderStatus(id) {
	var form = document.getElementById("form"+id);
	var status = form.elements["status"].value;
	
	var updateOrder = { "id" : id, "status" : status };
	var updateOrderJSON = JSON.stringify(updateOrder);
	
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var updateFlag = this.responseText;
			
			if (updateFlag == 1){
				var div = document.getElementById('innerDiv'+id);
				div.innerHTML = '<B>Current Status: </B>'+ status.toUpperCase();
				
				if (status == "collected"){
					removeOrder(id);
				}
			}
		}
		else if (this.status == 404){
			var html = '<h2>404: Page Not Found.</h2><br> Try Login <a href="login.html">Here</a>';
			var content = document.getElementById("all_orders");
		    content.innerHTML = html;
		}
	}
	request.open("POST","kitchenmanager");
	request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	request.send(updateOrderJSON);
}

function removeOrder(id) {
	var div = document.getElementById('div'+id);
    div.remove();
}

loadAllPendingOrders();
setInterval ( loadAllPendingOrders, 60000 );