" Vim syntax file
" Language: Kotlin
" Maintainer: Alexander Udalov
" Latest Revision: 28 February 2013

if exists("b:current_syntax")
  finish
endif

syn keyword ktStatement break continue return
syn keyword ktConditional if else when
syn keyword ktRepeat do for while
syn keyword ktOperator as in is by
syn keyword ktKeyword get set out super this This where
syn keyword ktException try catch finally throw

syn keyword ktInclude import package

syn keyword ktType Any Boolean Byte Char Double Float Int Long Nothing Short Unit
syn keyword ktModifier annotation data enum inner internal private protected public abstract final open override vararg
syn keyword ktStructure class fun object trait val var
syn keyword ktTypedef type

syn keyword ktBoolean true false
syn keyword ktConstant null

syn match ktLineComment "\/\/.*$"
syn region ktComment start="\/\*" end="\*\/"

syn region ktString start='"' skip='\\"' end='"'
syn region ktString start='"""' end='"""'
syn match ktCharacter "'.'"

syn match ktNumber "\<[0-9]\+"
syn match ktFloat "\<[0-9]\+\.[0-9]*"

syn match ktExclExcl "!!"
syn match ktArrow "->"



hi link ktStatement Statement
hi link ktConditional Conditional
hi link ktRepeat Repeat
hi link ktOperator Operator
hi link ktKeyword Keyword
hi link ktException Exception

hi link ktInclude Include

hi link ktType Type
hi link ktModifier StorageClass
hi link ktStructure Structure
hi link ktTypedef Typedef

hi link ktBoolean Boolean
hi link ktConstant Constant

hi link ktLineComment Comment
hi link ktComment Comment

hi link ktString String
hi link ktCharacter Character

hi link ktNumber Number
hi link ktFloat Float

hi link ktExclExcl Special
hi link ktArrow Structure

