let entries = document.getElementsByClassName("select-elem")
for(let i = 0; i < entries.length; i++){
    entries[i].addEventListener("click",entrybehavior);
}

function entrybehavior(){
    window.location.href = this.id

}