SET _urlEndpoint=%1
SET _urlParameters=%2
SET _tempFile=%3

curl -X POST -H "Content-Type:text/plain" "%_urlEndpoint%?%_urlParameters%" -o "%_tempFile%"