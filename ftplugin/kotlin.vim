if exists('b:did_ftplugin') | finish | endif
let b:did_ftplugin = 1

setlocal comments=sO:*\ -,mO:*\ \ ,exO:*/,s1:/*,mb:*,ex:*/,://
setlocal commentstring=//\ %s

" Function to sort import statements in Kotlin file
python3 << EOL
import re
def SortImports():
    """Sort the imports from the buffer """

    buffer_lines = vim.current.buffer

    start = 0
    end = 0

    for i, value in enumerate(buffer_lines):


        if re.match("^$", buffer_lines[i]):

            # If we have reached the imports yet, just set the start index to current line
            if end == 0:
                start = i

            # We are at the end
            else:
                break

        if re.match("^import ", buffer_lines[i]):
            end = i

    # Start is currently a blank line.
    # Move it to the first import statement
    start = start + 1

    # End is at the last line, we want this to be the blank
    # line after the import list
    end = end + 1

    # Sort the lines in the provided range
    imports = sorted(buffer_lines[start:end])

    # Replace the lines
    for i, value in enumerate(imports):
        buffer_lines[i + start] = value

EOL
map <F6> :python3 SortImports()<CR>

autocmd BufWrite *.kt :python3 SortImports()

