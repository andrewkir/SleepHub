// function enlarge(e, state){
//     e.querySelectorAll('i')[0].style.fontSize = `${state}px`;
// }


// function toggleLike(id){
//     fetch("/abc", {
//         method: "POST",
//         headers: {'Content-Type': 'application/json'},
//         body: JSON.stringify({
//             id: id
//         })
//     })
//     .then(function(res){
//         return res.json();
//     })
//     .then(function(data){
//         document.querySelector(`#like${id}`).innerHTML = data.ammount;
//         document.querySelector(`#icon${id}`).style.color = data.color;
//     });
// }