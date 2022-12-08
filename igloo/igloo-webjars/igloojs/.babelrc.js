module.exports = {
    presets: [
      [
        '@babel/preset-env',
        {
          loose: false,
          bugfixes: true,
          modules: false,
          exclude: ["@babel/plugin-transform-classes"]
        }
      ]
    ],
    env: {}
}  