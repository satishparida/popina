function loadProducts(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var allProducts = JSON.parse(this.responseText);
			var html = '<div> <form id="addProduct">';
			html += '<input type="button" value="New Product" onClick="addProduct();">';
			html += '</form> </div> <br>';
			for (product in allProducts) {
				html += '<div id="div' + allProducts[product].productId + '">';
				html += '<form id="form' + allProducts[product].productId + '">';
				
				html += '<B>Name: </B><input type="text" name="productName" value="' 
									+ allProducts[product].productName + '"/>';
				html += '<B> Cost: </B><input type="text" name="productCost" value="' 
									+ allProducts[product].productCost + '"/>';
				html += '<B> Avalibility: </B><input type="text" name="productAvalibility" value="' 
									+ allProducts[product].productAvalibility + '"/>';
				html += '<B> Preparation Time: </B><input type="text" name="productPreparationTime" value="' 
									+ allProducts[product].productPreparationTime + '"/>';
				html += ' <input type="button" value="Update" onClick="updateProduct(' + allProducts[product].productId + ');">';
				html += ' <input type="button" value="Remove" onClick="removeProduct(' + allProducts[product].productId + ');">';
				
				html += '</form> </div> <br>';
			}
		}
		else if (this.status == 404){
			var html = '<h2>404: Page Not Found.</h2><br> Try Login <a href="login.html">Here</a>';
			var content = document.getElementById("all_products");
		    content.innerHTML = html;
		}
		var content = document.getElementById("all_products");
	    content.innerHTML = html;
	    
	    html = '<a href="logout">logout</a><br>';
	    content = document.getElementById("logout");
	    content.innerHTML = html;
	}
	request.open("GET","inventorymanager",true);
	request.send();
}

function updateProduct(id){
	var form = document.getElementById("form"+id);
	var productName = form.elements["productName"].value;
	var productCost = form.elements["productCost"].value;
	var productAvalibility = form.elements["productAvalibility"].value;
	var productPreparationTime = form.elements["productPreparationTime"].value;
	
	var updateProduct = { "action" : "update",
							"id" : id,
							"productname" : productName, 
							"productcost" : productCost, 
							"productavalibility" : productAvalibility, 
							"productpreparationtime" : productPreparationTime };
	var updateProductJSON = JSON.stringify(updateProduct);
	
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var updateFlag = this.responseText;
			
			if (updateFlag == 1){
				window.alert("Product "+ id +" Updated.");
			}
		}
		else if (this.status == 404){
			var html = '<h2>404: Page Not Found.</h2><br> Try Login <a href="login.html">Here</a>';
			var content = document.getElementById("all_products");
		    content.innerHTML = html;
		}
	}
	
	request.open("POST","inventorymanager");
	request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	request.send(updateProductJSON);
}

function removeProduct(id){
	if (confirm("Are you sure to Remove/Delete Product: "+id)) {
		var removeProduct = { "action" : "remove",
								"id" : id};
		var removeProductJSON = JSON.stringify(removeProduct);
		
		var request = new XMLHttpRequest();
		request.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				var removeFlag = this.responseText;
				
				if (removeFlag == 1){
					var div = document.getElementById('div'+id);
				    div.remove();
					window.alert("Product "+ id +" Removed.");
				}
			}
			else if (this.status == 404){
				var html = '<h2>404: Page Not Found.</h2><br> Try Login <a href="login.html">Here</a>';
				var content = document.getElementById("all_products");
			    content.innerHTML = html;
			}
		}
		
		request.open("POST","inventorymanager");
		request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		request.send(removeProductJSON);
	}
}

function addProduct(){
	if (confirm("Are you sure to add Product")) {
		var html = '<div id="addProductDivFn"><form id="addProduct_fn">';
		html += '<B>Name: </B><input type="text" name="newProductName" value=""/> <br> <br>' ;
		html += '<B>Cost: </B><input type="text" name="newProductCost" value=""/> <br> <br>' ;
		html += '<B>Avalibility: </B><input type="text" name="newProductAvalibility" value=""/> <br> <br>' ;
		html += '<B>Preparation Time: </B><input type="text" name="newProductPreparationTime" value=""/> <br> <br>';
		html += '<input type="button" value="Create Product" onClick="addProduct_fn();">';
		html += '</form> </div> <br>';
		
		var div = document.getElementById("add_product");
	    div.innerHTML = html;
	}
}

function addProduct_fn(){
	var form = document.getElementById("addProduct_fn");
	var productName = form.elements["newProductName"].value;
	var productCost = form.elements["newProductCost"].value;
	var productAvalibility = form.elements["newProductAvalibility"].value;
	var productPreparationTime = form.elements["newProductPreparationTime"].value;
	
	var addProduct = { "action" : "add",
						"productname" : productName, 
						"productcost" : productCost, 
						"productavalibility" : productAvalibility, 
						"productpreparationtime" : productPreparationTime };
	var addProductJSON = JSON.stringify(addProduct);
	
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var addFlag = this.responseText;
			
			if (addFlag == 1){
				window.alert("Product Added Successfully.");
				var rmdiv = document.getElementById('addProductDivFn');
				rmdiv.remove();
				loadProducts();
			}
		}
		else if (this.status == 404){
			var html = '<h2>404: Page Not Found.</h2><br> Try Login <a href="login.html">Here</a>';
			var content = document.getElementById("all_products");
		    content.innerHTML = html;
		}
	}
	
	request.open("POST","inventorymanager");
	request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	request.send(addProductJSON);
}