# kotlin-vim

## Installation

### [Vundle](https://github.com/gmarik/Vundle.vim)

Add `Plugin 'udalov/kotlin-vim'` to your `~/.vimrc` and run `PluginInstall`.

### [Pathogen](https://github.com/tpope/vim-pathogen)

    $ git clone https://github.com/udalov/kotlin-vim ~/.vim/bundle/kotlin-vim

### [Vim8 Native](https://shapeshed.com/vim-packages/)

You can also use Vim 8 built-in package manager:

`mkdir ~/.vim/pack/default/start`
`cd ~/.vim/pack/default/start`
`git clone https://github.com/udalov/kotlin-vim.git`


### Manual

0. `mkdir -p ~/.vim/{syntax,indent,ftdetect}`
1. `cp syntax/kotlin.vim ~/.vim/syntax/kotlin.vim`
2. `cp indent/kotlin.vim ~/.vim/indent/kotlin.vim`
3. `cp ftdetect/kotlin.vim ~/.vim/ftdetect/kotlin.vim`
4. If you use [Syntastic](https://github.com/scrooloose/syntastic): `cp -r syntax_checkers/kotlin ~/.vim/syntax_checkers/`
5. Restart Vim

##### Enjoy!
