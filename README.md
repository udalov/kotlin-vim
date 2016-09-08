# kotlin-vim

## Installation

### [Vundle](https://github.com/gmarik/Vundle.vim)

Add `Plugin 'udalov/kotlin-vim'` to your `~/.vimrc` and run `PluginInstall`.

### [Pathogen](https://github.com/tpope/vim-pathogen)

    $ git clone https://github.com/udalov/kotlin-vim ~/.vim/bundle/kotlin-vim

### Manual

0. `mkdir -p ~/.vim/{syntax,indent,ftdetect}`
1. `cp syntax/kotlin.vim ~/.vim/syntax/kotlin.vim`
2. `cp indent/kotlin.vim ~/.vim/indent/kotlin.vim`
3. `cp ftdetect/kotlin.vim ~/.vim/ftdetect/kotlin.vim`
4. If you use [Syntastic](https://github.com/scrooloose/syntastic): `cp -r syntax_checkers/kotlin ~/.vim/syntax_checkers/`
5. Restart Vim

##### Enjoy!
