list = document.querySelectorAll(".headerText");
for (index = 0; index < list.length; ++index) {
    list[index].setAttribute("onmouseover", "a(this, 100)");
    list[index].setAttribute("onmouseout", "a(this, 0)");
}
function a(e, state){
    e.querySelector(".hoverLine").style.width = `${state}%`;
}
window.onscroll = function() {
    var scrolled = document.documentElement.scrollTop;
    if(scrolled){
        document.getElementById("header").style.background = "white";
        document.querySelectorAll(".headerText").forEach(el => {
                el.style.color = "black";
        });
        document.querySelectorAll(".hoverLine").forEach(el => {
                el.style.background = "black";
        });
    }
    if(!scrolled){
        document.getElementById("header").style.background = "rgba(243, 243, 243, 0)";
        document.querySelectorAll(".headerText").forEach(el => {
                el.style.color = "white";
        });
        document.querySelectorAll(".hoverLine").forEach(el => {
                el.style.background = "white";
        });
    }
}