" Vim filetype plugin file
" Language:     Kotlin
" Maintainer:   Alexander Udalov
" URL:          https://github.com/udalov/kotlin-vim
" Last Change:  7 November 2021

if exists('b:did_ftplugin') | finish | endif
let b:did_ftplugin = 1

setlocal comments=sO:*\ -,mO:*\ \ ,exO:*/,s1:/*,mb:*,ex:*/,://
setlocal commentstring=//\ %s

setlocal includeexpr=substitute(v:fname,'\\.','/','g')
setlocal suffixesadd=.kt
