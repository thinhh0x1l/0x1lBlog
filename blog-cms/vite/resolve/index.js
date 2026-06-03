import createAlias from "./createAlias.js";

export default function createViteResolve(){
    return {
        alias: createAlias(),
        extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    }
}