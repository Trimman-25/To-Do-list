    const input=document.getElementById("input")
    const work=document.getElementById("work");
    const btn=document.getElementById("btn");
    const ul=document.getElementById("content");
    const title=document.querySelector("h1")
    btn.addEventListener("click",()=> {

    // Taking Input from the user that is giving

    const task=input.value.trim();
    if(task==="") return;


  const li=document.createElement("li");
  const span=document.createElement("span");
  span.innerText=task;

  

  const editingImg=document.createElement("img")
  editingImg.src="https://cdn-icons-png.flaticon.com/512/4400/4400968.png"
  editingImg.style.height="20px";
  editingImg.style.cursor="pointer";
  editingImg.style.marginLeft="10px";

  const removeImg=document.createElement("img");
  removeImg.src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTC8rerEOUKgP7ClwJerKINCB8ZmX9UNVWAmw&s";
  removeImg.style.height="20px";
  removeImg.style.cursor="pointer";
  removeImg.style.marginLeft="10px";

  li.appendChild(span)
  li.appendChild(editingImg);
  li.appendChild(removeImg);
  ul.appendChild(li)
    input.value="";
        removeImg.addEventListener("click", () => {
        ul.removeChild(li)


    });

 editingImg.addEventListener("click", () => {
    const current = span.innerText;
    const inputBox = document.createElement("input");
    inputBox.type = "text";
    inputBox.value = current;
    inputBox.style.marginLeft = "5px";
    inputBox.style.background = "transparent";
    inputBox.style.border = "1px solid rgba(255,255,255,0.5)";
    inputBox.style.color = "white";
    inputBox.style.padding = "5px";
    inputBox.style.borderRadius = "5px";
    
    li.replaceChild(inputBox, span);
    inputBox.focus();
      inputBox.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            const newText = inputBox.value.trim();
            if (newText !== "") {
                span.innerText = newText;
                li.replaceChild(span, inputBox);
            }
        }
    });
    })
})
