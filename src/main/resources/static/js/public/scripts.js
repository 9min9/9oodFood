
window.addEventListener('DOMContentLoaded', event => {
  // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');

    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            event.stopPropagation();
            window.dispatchEvent(new Event('resize'));
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });




        // Close sidebar when clicking outside
        document.addEventListener('click', event => {
            if (!event.target.closest('.sb-sidenav')) {
                document.body.classList.remove('sb-sidenav-toggled');
                window.dispatchEvent(new Event('resize'));

//                  if (document.body.classList.contains('sb-sidenav-toggled') && !sidebarToggle.contains(event.target)) {
//                    document.body.classList.remove('sb-sidenav-toggled');
//                    localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
//
//                  }
                }





//            if (!event.target.closest('.sb-sidenav') && !event.target.closest('#sidebarToggle')) {
//                if (document.body.classList.contains('sb-sidenav-toggled')) {
//                    document.body.classList.remove('sb-sidenav-toggled');
//                    localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
//                }
//            }
        });
    }

    //Function to toggle sidebar
    function toggleSidebar() {
        document.body.classList.toggle('sb-sidenav-toggled');
        localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
    }
});





/*****************/

//window.addEventListener('DOMContentLoaded', event => {
//  // Toggle the side navigation
//    const sidebarToggle = document.body.querySelector('#sidebarToggle');
//    const sidenavAccordion = document.querySelector('#sidenavAccordion');
//
//    if (sidebarToggle && sidenavAccordion) {
//        sidebarToggle.addEventListener('click', event => {
//            window.dispatchEvent(new Event('resize'));
//            event.preventDefault();
//            toggleSidebar();
//        });
//
//        // Close sidebar when clicking outside
//        document.addEventListener('click', event => {
//            if (!event.target.closest('.sb-sidenav') && !event.target.closest('#sidebarToggle')) {
//                if (document.body.classList.contains('sb-sidenav-toggled')) {
//                    toggleSidebar();
//                }
//            }
//        });
//    }
//
//    //Function to toggle sidebar
//    function toggleSidebar() {
//        document.body.classList.toggle('sb-sidenav-toggled');
//        localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
//    }
//});



/*!  Original
    * Start Bootstrap - SB Admin v7.0.5 (https://startbootstrap.com/template/sb-admin)
    * Copyright 2013-2022 Start Bootstrap
    * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
    */

//window.addEventListener('DOMContentLoaded', event => {
//    // Toggle the side navigation
//    const sidebarToggle = document.body.querySelector('#sidebarToggle');
//
//    if (sidebarToggle) {
//        sidebarToggle.addEventListener('click', event => {
//            event.preventDefault();
//            window.dispatchEvent(new Event('resize'));
//            document.body.classList.toggle('sb-sidenav-toggled');
//            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
//
//        });
//    }
//});