" Vim syntastic plugin
" Language: Kotlin

if exists('g:loaded_syntastic_kotlin_kotlinc_checker')
	finish
endif
let g:loaded_syntastic_kotlin_kotlinc_checker = 1

let s:save_cpo = &cpo
set cpo&vim

if !exists('g:syntastic_kotlin_kotlinc_options')
	let g:syntastic_kotlin_kotlinc_options = ""
endif

if !exists('g:syntastic_kotlin_kotlinc_delete_output')
	let g:syntastic_kotlin_kotlinc_delete_output = 1
endif

if !exists('g:syntastic_kotlin_kotlinc_config_file_enabled')
	let g:syntastic_kotlin_kotlinc_config_file_enabled = 0
endif

if !exists('g:syntastic_kotlin_kotlinc_config_file')
	let g:syntastic_kotlin_kotlinc_config_file = ".syntastic_kotlinc_config"
endif

if !exists('g:syntastic_kotlin_kotlinc_classpath')
	let g:syntastic_kotlin_kotlinc_classpath = ""
endif

if !exists('g:syntastic_kotlin_kotlinc_sourcepath')
	let g:syntastic_kotlin_kotlinc_sourcepath = ""
endif

function! SyntaxCheckers_kotlin_kotlinc_GetLocList() dict
	let kotlinc_opts = g:syntastic_kotlin_kotlinc_options

	if g:syntastic_kotlin_kotlinc_config_file_enabled
		if filereadable(expand(g:syntastic_kotlin_kotlinc_config_file, 1))
			execute "source " . fnameescape(expand(g:syntastic_kotlin_kotlinc_config_file, 1))
		endif
	endif

	if g:syntastic_kotlin_kotlinc_classpath !=# ""
		let kotlinc_opts .= " -cp " . g:syntastic_kotlin_kotlinc_classpath
	endif

	let fname = ""
	if g:syntastic_kotlin_kotlinc_sourcepath !=# ""
		let fname .= expand(g:syntastic_kotlin_kotlinc_sourcepath, 1) . " "
	endif
	let fname .=  shellescape(expand("%", 1))


	let output_dir = ""
	if g:syntastic_kotlin_kotlinc_delete_output
		let output_dir = syntastic#util#tmpdir()
		let kotlinc_opts .= " -d " . syntastic#util#shescape(output_dir)
	endif

	let makeprg = self.makeprgBuild({
		\ "exe": "kotlinc",
		\ "args": kotlinc_opts,
		\ "fname": fname })

	let errorformat =
		\ "%E%f:%l:%c: error: %m," .
		\ "%W%f:%l:%c: warning: %m," .
		\ "%Eerror: %m," .
		\ "%Wwarning: %m," .
		\ "%Iinfo: %m,"

	if output_dir !=# ''
		silent! call mkdir(output_dir, 'p')
	endif

	let errors = SyntasticMake({
		\ 'makeprg': makeprg,
		\ 'errorformat': errorformat, })

	if output_dir !=# ''
		call syntastic#util#rmrf(output_dir)
	endif

	let currbufnr = bufnr("%")
	let relevant_errors = []

	for error in errors
		" Only get messages bounded to this buffer or are 'bufferless'
		if (error.bufnr == currbufnr) || (error.bufnr == 0)
			let relevant_errors = add(relevant_errors, error)
		endif
	endfor

	return relevant_errors
endfunction

call g:SyntasticRegistry.CreateAndRegisterChecker({
	\ 'filetype': 'kotlin',
	\ 'name': 'kotlinc',
	\ 'exec': 'kotlinc' })

let &cpo = s:save_cpo
unlet s:save_cpo
