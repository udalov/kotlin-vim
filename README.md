# kotlin-vim

## Installation

### Vim 8 and later

Vim since version 8 has built-in package support

    $ mkdir -p ~/.vim/pack/kotlin-vim/start
    $ cd ~/.vim/pack/kotlin-vim/start
    $ git clone https://github.com/udalov/kotlin-vim.git

Restart Vim

### [Vundle](https://github.com/gmarik/Vundle.vim)

Add `Plugin 'udalov/kotlin-vim'` to your `~/.vimrc` and run `PluginInstall`.

### [Pathogen](https://github.com/tpope/vim-pathogen)

    $ git clone https://github.com/udalov/kotlin-vim ~/.vim/bundle/kotlin-vim

### Manual, for older versions of Vim

0. `mkdir -p ~/.vim/{syntax,indent,ftdetect,ftplugin}`
1. `cp syntax/kotlin.vim ~/.vim/syntax/kotlin.vim`
2. `cp indent/kotlin.vim ~/.vim/indent/kotlin.vim`
3. `cp ftdetect/kotlin.vim ~/.vim/ftdetect/kotlin.vim`
4. `cp ftplugin/kotlin.vim ~/.vim/ftplugin/kotlin.vim`
5. If you use [Syntastic](https://github.com/scrooloose/syntastic): `cp -r syntax_checkers/kotlin ~/.vim/syntax_checkers/`
6. Restart Vim

##### Enjoy!
