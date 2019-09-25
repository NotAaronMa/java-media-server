//html DOM elements
let vlist = document.getElementsByTagName("li");
let player  = document.getElementById("player");
let vsource = document.getElementById("video-source");
//media source object

for(let i = 0; i < vlist.length; i++){
    vlist[i].addEventListener("click",streamVideo);

}

function streamVideo(){
    vsource.src=this.id;
    player.load();
    for(let i = 0 ;i < vlist.length;i ++){
        vlist[i].style= null;
    }
    this.style = "background: rgb(20,20,20);"

}




