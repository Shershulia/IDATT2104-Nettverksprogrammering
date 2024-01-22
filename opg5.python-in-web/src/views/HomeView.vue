<template>
  <div class="home">
    <div>
      <textarea v-model="inputText" />
    </div>
    <button @click="runCode">Run code</button>
    <div>
      <textarea v-model="outputText" readonly />
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "HomeView",
  data() {
    return {
      inputText: "",
      outputText: "",
    };
  },
  methods: {
    runCode() {
      axios
        .post("http://localhost:5000", {
          code: this.inputText,
        })
        .then((response) => {
          this.outputText = response.data.result;
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style>
textarea {
  width: 30%;
  height: 150px;
  padding: 12px 20px;
  box-sizing: border-box;
  border: 2px solid #ccc;
  border-radius: 4px;
  background-color: #f8f8f8;
  resize: none;
}
button {
  height: 70px;
  border: 2px solid green;
  border-radius: 4px;
  background-color: #f8f8f8;
  margin: 10px;
}
</style>
