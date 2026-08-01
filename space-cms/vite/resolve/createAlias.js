
import path from 'node:path'
export default function createAlias(){
    const root = process.cwd()
    return {
        '@': path.resolve(root, 'src'),
        '@icons': path.resolve(root, 'src/assets/icons'),
        'components': path.resolve(root, 'src/components'),
        'network': path.resolve(root, 'src/network'),
        'plugins': path.resolve(root, 'src/plugins'),
        'router': path.resolve(root, 'src/router')
    }
}