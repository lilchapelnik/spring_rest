fetch("http://localhost:8080/api/about").then(
    res=>{
        res.json().then(
            data=> {
                let temp = `<tr>
                         <td>${data.id}</td>
                         <td>${data.name}</td>  
                         <td>${data.lastname}</td>  
                         <td>${data.age}</td>  
                         <td>${data.mail}</td>  
                         <td>${data.rolesName}</td></tr>`
                document.getElementById("tableBody").innerHTML = temp;
            }
        )
    }
)