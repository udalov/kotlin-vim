# kotlin-vim

## Installation

If you use Vim 8 or later, the recommended way is using [Vim's built-in package manager](https://vimhelp.org/repeat.txt.html#packages):

    git clone https://github.com/udalov/kotlin-vim.git ~/.vim/pack/plugins/start/kotlin-vim

#### Other plugin managers:

* [Vundle](https://github.com/gmarik/Vundle.vim)
    * add `Plugin 'udalov/kotlin-vim'` to your `~/.vimrc` and run `PluginInstall`.
* [Pathogen](https://github.com/tpope/vim-pathogen)
    * `git clone https://github.com/udalov/kotlin-vim ~/.vim/bundle/kotlin-vim`

#### Manual (for older versions of Vim)

0. `mkdir -p ~/.vim/{syntax,indent,ftdetect,ftplugin}`
1. `cp syntax/kotlin.vim ~/.vim/syntax/kotlin.vim`
2. `cp indent/kotlin.vim ~/.vim/indent/kotlin.vim`
3. `cp ftdetect/kotlin.vim ~/.vim/ftdetect/kotlin.vim`
4. `cp ftplugin/kotlin.vim ~/.vim/ftplugin/kotlin.vim`
5. If you use [Syntastic](https://github.com/scrooloose/syntastic): `cp -r syntax_checkers/kotlin ~/.vim/syntax_checkers/`
6. Restart Vim

##### Enjoy!
