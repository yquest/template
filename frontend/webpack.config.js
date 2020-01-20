const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const webpack = require('webpack');
const path = require('path');
const http = require('http');

const basePath = __dirname;
var testServices = false;

module.exports = [function (env, argv) {
  const base = {
    context: path.join(basePath, "src"),
    resolve: {
      extensions: ['.js', '.ts', '.tsx']
    },
    output: {
      path: path.join(basePath, 'dist')
    },
    devServer: {
      contentBase: './dist', // Content base
      //inline: true, // Enable watch and live reload
      host: 'localhost',
      port: 8080,
      stats: 'errors-only',
      before: function (app, server) {
        app.get('/some/path', function (req, res) {
          res.json({ custom: 'res' });
        });
      },
      proxy: {
        '/api': {
          target: 'http://localhost:8888',
          secure: false
        }
      }
    },
    module: {
      rules: [
        {
          test: /\.(ts|tsx)$/,
          exclude: /node_modules/,
          loader: 'awesome-typescript-loader',
          options: {
            useBabel: true,
            "babelCore": "@babel/core", // needed for Babel v7
          },
        },
        {
          test: /\.(sa|sc|c)ss$/,
          use: [MiniCssExtractPlugin.loader, "css-loader", "sass-loader"]
        },
        {
          test: /\.(png|jpg|gif|svg)$/,
          loader: 'file-loader',
          options: {
            name: 'assets/img/[name].[ext]?[hash]'
          }
        },
        { test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: 'file-loader?mimetype=image/svg+xml' },
        { test: /\.woff(\?v=\d+\.\d+\.\d+)?$/, loader: "file-loader?mimetype=application/font-woff" },
        { test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/, loader: "file-loader?mimetype=application/font-woff" },
        { test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: "file-loader?mimetype=application/octet-stream" },
        { test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: "file-loader" }

      ],
    },
    plugins: [
      new MiniCssExtractPlugin({
        filename: "[name].css",
        chunkFilename: "[id].css"
      })
    ],
  };

  var lenv = env || {    
    platform: 'no-ssr'
  };

  lenv.platform = lenv.platform || 'no-ssr';

  console.log(`env: ${JSON.stringify(env)}`);

  // no ssr
  if (lenv.platform === 'no-ssr') {
    base.entry = ['@babel/polyfill',
      './content/styles.scss',
      './NOSSR.tsx'
    ]
    base.output.filename = 'bundle.js';

    base.plugins.push(
      new HtmlWebpackPlugin({
        filename: 'index.html', //Name of file in ./dist/
        template: 'index.html', //Name of template in ./src
        favicon: "favicon.ico",
        hash: true,
      })
    );
  }
  //ssr
  else if (lenv.platform === 'ssr') {
    base.entry = ['@babel/polyfill',
      './content/styles.scss',
      './SSR.tsx'
    ]
    base.output.filename = 'bundle.js';
  }
  //webserver
  else if (lenv.platform === 'webserver') {
    base.entry = ['@babel/polyfill',
      './content/styles.scss',
      './NOSSR.tsx'
    ]
    base.output.filename = 'bundle.js';

    base.plugins.push(
      new HtmlWebpackPlugin({
        filename: 'index.html', //Name of file in ./dist/
        template: 'index.html', //Name of template in ./src
        favicon: "favicon.ico",
        hash: true,
      })
    );
    testServices = true;

    base.devServer.before = (app, server, compiler) => {
      app.get('/some/path', function(req, res) {
      http.get('http://localhost:8888/api/init-data', (resp) => {
        let data = '';

        // A chunk of data has been recieved.
        resp.on('data', (chunk) => {
          data += chunk;
        });

        // The whole response has been received. Print out the result.
        resp.on('end', () => {
          console.log("data",data);
          res.json({ response: 'cool' });
        });

      }).on("error", (err) => {
        res.json({ response: 'error' });
        console.log("Error: " + err.message);
      });
      });
    }
  }
  console.log(`test services?${testServices}`)
  base.plugins.push(new webpack.DefinePlugin({
    __APP_SERVICES_TEST__: JSON.stringify(testServices)
  }));
  return base;
},
function (env, argv) {
  const base = {
    context: path.join(basePath, "src"),
    resolve: {
      extensions: ['.js', '.ts', '.tsx']
    },
    output: {
      path: path.join(basePath, 'dist')
    },
    module: {
      rules: [
        {
          test: /\.(ts|tsx)$/,
          exclude: /node_modules/,
          loader: 'awesome-typescript-loader',
          options: {
            useBabel: true,
            "babelCore": "@babel/core", // needed for Babel v7
          }
        }
      ]
    }
  };

  // client-specific configurations
  base.entry = ['./sw.ts'];
  base.output.filename = 'sw.js';
  return base;
}
];
