SET _urlEndpoint=%1
SET _urlParameters=%2
SET _tempFile=%3

curl -X POST "%_urlEndpoint%" -o "%_tempFile%"