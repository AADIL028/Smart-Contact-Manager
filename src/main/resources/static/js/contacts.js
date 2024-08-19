// console.log("contact.js");
const baseURL = "http://localhost:8081";
const viewContactModal = document.getElementById("contact_modal");

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'contact_modal',
  override: true
};

const contactModal = new Modal(viewContactModal,options,instanceOptions);

function openContactModal(){
    contactModal.show();
}

async function loadContactData(id){
    // console.log(id);
    try {
        const data = await(await fetch(`${baseURL}/api/contacts/${id}`)).json();
        // console.log(data);
        document.getElementById("contact_name").innerHTML=data.name;
        document.getElementById("contact_email").innerHTML=data.email;
        document.getElementById("contact_phone").innerHTML=data.phoneNumber;
        document.getElementById("contact_address").innerHTML=data.address;
        if(data.favourite){
        document.getElementById("contact_favourite").innerHTML="YES";
        } else{
            document.getElementById("contact_favourite").innerHTML="NO";

        }
        document.getElementById("contact_description").innerHTML=data.description;
        document.getElementById("contact_website").innerHTML=data.websiteLink;
        document.getElementById("contact_website").href=data.websiteLink;
        document.getElementById("contact_linkedin").innerHTML=data.linkedinLink;
        document.getElementById("contact_linkedin").href=data.linkedinLink;
        let pic = data.picture;
        if (data.picture == null) {
            pic = "https://i.pinimg.com/originals/f1/0f/f7/f10ff70a7155e5ab666bcdd1b45b726d.jpg";
        }
        document.getElementById("contact_image").src=pic;
        openContactModal();
    } catch (error) {
        console.log("error : ",error);
    }
}

async function deleteContact(id){
    Swal.fire({
        title: "Do you want to delete the contact?",
        showCancelButton: true,
        icon:"warning",
        confirmButtonText: "Delete",
        confirmButtonColor: "#dc3741",
        cancelButtonColor: "#6e7881"
      }).then((result) => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
            window.location.replace(`${baseURL}/user/contacts/delete/`+id);
          Swal.fire("Deleted!", "", "success");
        } 
      });
}