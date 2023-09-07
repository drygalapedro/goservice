import { initializeApp } from "https://www.gstatic.com/firebasejs/10.3.1/firebase-app.js";
import { getAnalytics } from "https://www.gstatic.com/firebasejs/10.3.1/firebase-analytics.js";
import { getStorage, ref, uploadBytes, getDownloadURL } from 'https://www.gstatic.com/firebasejs/10.3.1/firebase-storage.js';

const firebaseApp = {
   apiKey: "AIzaSyCsa_y3PeVafIAq7sNGwfM678bMYMDOKqA",
   authDomain: "goservice-8f9eb.firebaseapp.com",
   projectId: "goservice-8f9eb",
   storageBucket: "goservice-8f9eb.appspot.com",
   messagingSenderId: "223982970540",
   appId: "1:223982970540:web:d3394e9d6c337c6269e109"
};


// Initialize Firebase
const app = initializeApp(firebaseApp);

// Initialize Firebase
const storage = getStorage(app);

var fileInput = document.getElementById('profilePic');
var progressBar = document.getElementById('uploadProgressBar');
var updateButton = document.getElementById('updateButton');
var uploadDone = false;
updateButton.disabled = true;

fileInput.addEventListener('change', function() {
    var filePic = fileInput.files[0];

    if (filePic) {
        const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg'];
        if (!allowedTypes.includes(filePic.type)) {
            alert('Tipo de arquivo não permitido. Por favor, envie um arquivo JPEG/JPG ou PNG.');
            fileInput.value = '';  // Limpar o input
            return;
        }

        const maxFileSize = 5 * 1024 * 1024;  // 5 MB
        if (filePic.size > maxFileSize) {
            alert('O arquivo é muito grande. O tamanho máximo permitido é de 5 MB.');
            fileInput.value = '';  // Limpar o input
            return;
        }

        var reader = new FileReader();
        reader.onload = function(e) {
            var imageElement = document.getElementById('imagePreview');
            imageElement.src = e.target.result;

            updateButton.disabled = false;
        };
        reader.readAsDataURL(filePic);
    }
});

updateButton.addEventListener('click', async function() {
    var filePic = fileInput.files[0];

    const storageRef = ref(storage, 'profilePictures/' + filePic.name);
    const uploadTask = uploadBytes(storageRef, filePic);

    try {
        const snapshot = await uploadTask;
        const downloadURL = await getDownloadURL(snapshot.ref);
        alert("Imagem enviada com sucesso. ");
        fotoUsuario.src = downloadURL;
        urlFoto.value = downloadURL;
      } catch (error) {
        console.error(error);
      }
});


document.getElementById('btnSalvar').addEventListener('click', function() {
    uploadDone = false;
});
window.addEventListener('beforeunload', function(e) {
    if (uploadDone) {
        e.preventDefault();
        e.returnValue = 'Você fez o upload de uma imagem mas não clicou em Salvar. Tem certeza de que deseja sair?';
    }
});
