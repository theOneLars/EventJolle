const
  fs = require('fs'),
  path = require('path');


const envFileName = 'env-vars.json';
const envFilePath = path.join(__dirname, '..', 'src/environments', envFileName);

function generateEnvProperties(){
  const envProperties = {};

  envProperties.googleApiKey = process.env.GOOGLE_API_KEY;

  fs.writeFile(envFilePath, JSON.stringify(envProperties), function(err) {
    if(err) {
      return console.log(err);
    }

    console.log("Environment Variable Properties generated");
  });
}

generateEnvProperties();
