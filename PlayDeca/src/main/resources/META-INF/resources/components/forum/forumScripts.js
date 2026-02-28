// Forum JavaScript Functions
function toggleThread(threadId) {
    const item = document.querySelector(`[data-thread-id="${threadId}"]`);
    const content = item.querySelector('.thread-header-content');
    const button = item.querySelector('.toggle-button i');
    
    if (item.classList.contains('expanded')) {
        item.classList.remove('expanded');
        content.style.maxHeight = '60px';
        button.className = 'pi pi-chevron-down';
    } else {
        item.classList.add('expanded');
        content.style.maxHeight = 'none';
        button.className = 'pi pi-chevron-up';
    }
}

function backToThreadList() {
    // Hide post view and show thread list
    document.getElementById('threadList').style.display = 'block';
    document.getElementById('postView').style.display = 'none';
}